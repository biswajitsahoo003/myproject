package com.tcl.dias.oms.gsc.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderGscTfnRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.gsc.GscTestUtil;
import com.tcl.dias.oms.gsc.beans.GscOrderConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscTfnBean;
import com.tcl.dias.oms.gsc.service.v1.GscOrderDetailService;
import com.tcl.dias.oms.gsc.tiger.MockTigerServer;
import com.tcl.dias.oms.utils.GscObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.vavr.control.Try;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

/**
 * THis class contains all the test cases related to
 * {@link com.tcl.dias.oms.gsc.service.v1.GscOrderDetailService}
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GscOrderDetailServiceTest {
    private static MockWebServer mockTigerServer;
    @Autowired
    GscObjectCreator gscObjectCreator;
    @Autowired
    GscOrderDetailService gscOrderDetailService;
    @MockBean
    OrderRepository orderRepository;
    @MockBean
    OrderProductSolutionRepository orderProductSolutionRepository;
    @MockBean
    OrderGscRepository orderGscRepository;
    @MockBean
    OrderGscDetailRepository orderGscDetailRepository;
    @MockBean
    OrderProductComponentRepository orderProductComponentRepository;
    @MockBean
    ProductAttributeMasterRepository productAttributeMasterRepository;
    @MockBean
    OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;
    @MockBean
    OrderGscTfnRepository orderGscTfnRepository;
    @MockBean
    MQUtils mqUtils;

    @BeforeClass
    public static void init() {
        mockTigerServer = MockTigerServer.createServer();
        try {
            mockTigerServer.start(10001);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    @AfterClass
    public static void destroy() {
        if (mockTigerServer != null) {
            try {
                mockTigerServer.shutdown();
            } catch (IOException e) {
                Throwables.propagate(e);
            }
        }
    }

    @Test
    public void testUpdateOrderConfigurationsShouldFailForInvalidBusinessInputs() {
        Try<?> result = Try.of(() -> gscOrderDetailService.updateOrderConfigurations(null, 1, 1, ImmutableList.of()));
        assertTrue(result.isFailure());
        result = Try.of(() -> gscOrderDetailService.updateOrderConfigurations(1, null, 1, ImmutableList.of()));
        assertTrue(result.isFailure());
        result = Try.of(() -> gscOrderDetailService.updateOrderConfigurations(1, 1, null, ImmutableList.of()));
        assertTrue(result.isFailure());
        result = Try.of(() -> gscOrderDetailService.updateOrderConfigurations(1, 1, 1, null));
        assertTrue(result.isFailure());
    }

    @Test
    public void testUpdateOrderConfigurationsShouldBeSuccessfulForValidInput() {
        Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
                .thenReturn(gscObjectCreator.getOrders());
        Mockito.when(orderProductSolutionRepository.findById(Mockito.anyInt()))
                .thenReturn(gscObjectCreator.getOrderProductSolutionList().stream().findFirst());
        Mockito.when(orderGscRepository.findById(Mockito.anyInt()))
                .thenReturn(gscObjectCreator.getGscOrderList().stream().findFirst());
        Mockito.when(orderGscDetailRepository.findById(Mockito.anyInt()))
                .thenReturn(gscObjectCreator.getOrderGscDetailList().stream().findFirst());
        Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(gscObjectCreator.getOrderProductComponentLists());
        Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
                .thenReturn(gscObjectCreator.getProductAttributeMasterList());
        Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValue());
        List<GscOrderConfigurationBean> configurations = GscTestUtil.fromJsonFile(
                "com/tcl/dias/oms/gsc/controller/update_order_configuration_001.json",
                new TypeReference<List<GscOrderConfigurationBean>>() {
                });
        Try<?> result = Try.of(() -> gscOrderDetailService.updateOrderConfigurations(1, 1, 1, configurations));
        result.onFailure(Throwable::printStackTrace);
        assertTrue(result.isSuccess());
    }

    @Test
    public void testGetAvailableNumbersShouldFailForInvalidInputs() {
        Try<?> result = Try.of(() -> gscOrderDetailService.getAvailableNumbers(null, null, 10, null));
        assertTrue(result.isFailure());
        result = Try.of(() -> gscOrderDetailService.getAvailableNumbers(10, null, null, null));
        assertTrue(result.isFailure());
        result = Try.of(() -> gscOrderDetailService.getAvailableNumbers(10, null, 10, null));
        assertTrue(result.isFailure());
    }

    @Test
    public void testGetAvailableNumbersShouldBeSuccessForValidInputs() throws TclCommonException {
        OrderGscDetail orderGscDetail = gscObjectCreator.getOrderGscDetail();
        orderGscDetail.setOrderGsc(gscObjectCreator.getordergsc());
        orderGscDetail.getOrderGsc().setOrderToLe(gscObjectCreator.getOrderToLe());
        orderGscDetail.setOrderGscTfns(new HashSet<>());
        Mockito.when(orderGscDetailRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(orderGscDetail));
        Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("{\"accounCuId\":\"1\"}");
        Mockito.when(orderGscTfnRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
        Try<?> result = Try.of(() -> gscOrderDetailService.getAvailableNumbers(1, null, 10, true));
        result.onFailure(Throwable::printStackTrace);
        assertTrue(result.isSuccess());
    }

    @Test
    public void testSaveNumbersShouldFailForInvalidInputs() {
        Try<?> result = Try.of(() -> gscOrderDetailService.saveNumbers(null, null));
        assertTrue(result.isFailure());
        result = Try.of(() -> gscOrderDetailService.getAvailableNumbers(10, null, null, true));
        assertTrue(result.isFailure());
    }

    @Test
    public void testSaveNumbersShouldSucceedForValidInputs() throws TclCommonException {
        OrderGscDetail orderGscDetail = gscObjectCreator.getOrderGscDetail();
        orderGscDetail.setOrderGsc(gscObjectCreator.getordergsc());
        orderGscDetail.getOrderGsc().setOrderToLe(gscObjectCreator.getOrderToLe());
        orderGscDetail.setOrderGscTfns(new HashSet<>());
        Mockito.when(orderGscDetailRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(orderGscDetail));
        Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("{\"accounCuId\":\"1\"}");
        Mockito.when(orderGscTfnRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        GscTfnBean portedNumber = new GscTfnBean();
        portedNumber.setNumber("1234567");
        portedNumber.setPorted((byte) 1);
        GscTfnBean reservedNumber = new GscTfnBean();
        reservedNumber.setNumber("237463433");
        reservedNumber.setPorted((byte) 0);
        Try<?> result = Try
                .of(() -> gscOrderDetailService.saveNumbers(10, ImmutableList.of(portedNumber, reservedNumber)));
        result.onFailure(Throwable::printStackTrace);
        assertTrue(result.isSuccess());
    }

    /**
     * Positive test case for @link
     * {@link GscOrderDetailService#getCityNumberConfiguration(Integer, String)}
     */
    @Test
    public void testGetCityNumberConfigurationForLns() {
        Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(gscObjectCreator.getOrderProductComponentListsForLns());
        Mockito.when(orderProductComponentsAttributeValueRepository.findByOrderProductComponent(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValueListForLns());
        Mockito.when(orderGscTfnRepository.findByOrderGscDetailId(Mockito.anyInt()))
                .thenReturn(gscObjectCreator.getOrderGscTfnListForLns());
        Try<?> result = Try.of(() -> gscOrderDetailService.getCityNumberConfiguration(1, "LNS"));
        assertTrue(result.isSuccess());
    }

    /**
     * Positive test case for @link
     * {@link GscOrderDetailService#getCityNumberConfiguration(Integer, String)}
     */
    @Test
    public void testGetCityNumberConfigurationForLnsForNullTfns() {
        Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(gscObjectCreator.getOrderProductComponentListsForLns());
        Mockito.when(orderProductComponentsAttributeValueRepository.findByOrderProductComponent(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValueListForLns());
        Mockito.when(orderGscTfnRepository.findByOrderGscDetailId(Mockito.anyInt())).thenReturn(null);
        Try<?> result = Try.of(() -> gscOrderDetailService.getCityNumberConfiguration(1, "LNS"));
        assertTrue(result.isSuccess());
    }

    /**
     * Positive test case for @link
     * {@link GscOrderDetailService#getCityNumberConfiguration(Integer, String)}
     */
    @Test
    public void testGetCityNumberConfigurationForAcans() {
        Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(gscObjectCreator.getOrderProductComponentListsForAcans());
        Mockito.when(orderProductComponentsAttributeValueRepository.findByOrderProductComponent(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValueListForLns());
        Try<?> result = Try.of(() -> gscOrderDetailService.getCityNumberConfiguration(1, "ACANS"));
        assertTrue(result.isSuccess());
    }

}
