package com.tcl.dias.oms.gsc.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderGscTfnRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gsc.GscTestUtil;
import com.tcl.dias.oms.gsc.beans.GscApiRequest;
import com.tcl.dias.oms.gsc.beans.GscOrderConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscOrderSolutionBean;
import com.tcl.dias.oms.gsc.beans.GscTfnBean;
import com.tcl.dias.oms.gsc.controller.v1.GscOrderController;
import com.tcl.dias.oms.gsc.pdf.beans.GscOrderAttributesBean;
import com.tcl.dias.oms.gsc.tiger.MockTigerServer;
import com.tcl.dias.oms.gsc.util.GscConstants;
//import com.tcl.dias.oms.pdf.service.GscQuotePdfService;
import com.tcl.dias.oms.utils.GscObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.AfterClass;
import org.junit.Before;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Contains all test methods of Order Service.
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GscOrderControllerTest {

    private static MockWebServer mockTigerServer;
    @Autowired
    GscObjectCreator gscObjectCreator;
    /*@Autowired
    ObjectCreator omsObjectCreator;*/
//    @Autowired
//    GscQuotePdfService gscQuotePdfService;
    @Autowired
    GscOrderController gscOrderController;
    @MockBean
    OrderProductSolutionRepository orderProductSolutionRepository;
    @MockBean
    OrderGscRepository orderGscRepository;
    @MockBean
    OrderGscDetailRepository orderGscDetailRepository;
    @MockBean
    OrderRepository orderRepository;
    @MockBean
    OrderProductComponentRepository orderProductComponentRepository;
    @MockBean
    ProductAttributeMasterRepository productAttributeMasterRepository;
    @MockBean
    OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;
    @MockBean
    OrderToLeRepository orderToLeRepository;
    @MockBean
    OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;
    @MockBean
    OrderGscTfnRepository orderGscTfnRepository;
    @MockBean
    QuoteRepository quoteRepository;
    @MockBean
    QuoteToLeRepository quoteToLeRepository;
    @MockBean
    CofDetailsRepository cofDetailsRepository;
    @MockBean
    HttpServletResponse httpServletResponse;
    @MockBean
    QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;
    @MockBean
    ProductSolutionRepository productSolutionRepository;
    @MockBean
    OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;
    @MockBean
    MstOmsAttributeRepository mstOmsAttributeRepository;
    @MockBean
    MstProductFamilyRepository mstProductFamilyRepository;
    @MockBean
    MstOrderSiteStageRepository mstOrderSiteStageRepository;
    @MockBean
    MstOrderSiteStatusRepository mstOrderSiteStatusRepository;
    @MockBean
    MQUtils mqUtils;
    @MockBean
    UserInfoUtils userInfoUtils;

    @BeforeClass
    public static void initClass() {
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

    @Before
    public void init() throws TclCommonException {

        Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
                .thenReturn(gscObjectCreator.getOrders());
        Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
                .thenReturn(gscObjectCreator.getProductAttributeMasterList());
        Mockito.when(orderProductComponentsAttributeValueRepository
                .findAllByOrderProductComponentAndProductAttributeMaster_Id(Mockito.any(), Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValueList());
        doNothing().when(orderProductComponentsAttributeValueRepository).deleteAll(Mockito.anyIterable());
        Mockito.when(orderProductComponentsAttributeValueRepository.saveAll(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValueList());
        Mockito.when(orderProductSolutionRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(gscObjectCreator.getOrderProductSolution()));
        Mockito.when(orderGscRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(gscObjectCreator.getordergsc()));
        Mockito.when(orderGscDetailRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(gscObjectCreator.getOrderGscDetail()));
        Mockito.when(orderProductComponentRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(gscObjectCreator.getOrderProductComponent()));
        Mockito.when(orderToLeRepository.findByOrder(Mockito.any())).thenReturn(gscObjectCreator.getOrderToLeList());
        Mockito.when(orderToLeProductFamilyRepository.findByOrderToLe_Id(Mockito.any()))
                .thenReturn(gscObjectCreator.getorderToLeProductFamilies());
        Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductSolutionList());
        Mockito.when(orderGscRepository.findByorderProductSolutionAndStatus(Mockito.any(), Mockito.anyByte()))
                .thenReturn(gscObjectCreator.getGscOrderList());
        Mockito.when(orderGscDetailRepository.findByorderGsc(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderGscDetailList());
        Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentLists());
        Mockito.when(orderProductComponentsAttributeValueRepository.findByOrderProductComponent(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValueLists());
        Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.of(gscObjectCreator.getOrderToLes()));
        Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrdersLeAttributeArrayValueList());
        Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
                .thenReturn(gscObjectCreator.getMstOmsAttributeList());
        Mockito.when(ordersLeAttributeValueRepository.findByMstOmsAttributeAndOrderToLe(Mockito.any(), Mockito.any()))
                .thenReturn(gscObjectCreator.getOrdersLeAttributeArrayValueSet());
        Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrdersLeAttributeArrayValueList());
        doNothing().when(ordersLeAttributeValueRepository).deleteAll();
        Mockito.when(ordersLeAttributeValueRepository.saveAll(Mockito.any()))
                .thenReturn(new ArrayList<OrdersLeAttributeValue>());
        Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
                .thenReturn(gscObjectCreator.getMstProductFamily());
        Mockito.when(orderProductComponentsAttributeValueRepository
                .findByOrderProductComponentAndProductAttributeMaster(Mockito.any(), Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValueList());
        Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
                .thenReturn(gscObjectCreator.getProductAtrributeMaster());
        Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
                .thenReturn(gscObjectCreator.createOrderAttributes());
        Mockito.when(mstOrderSiteStageRepository.findByName(Mockito.anyString()))
                .thenReturn(gscObjectCreator.getMstOrderSiteStage());
        Mockito.when(mstOrderSiteStatusRepository.findByName(Mockito.anyString()))
                .thenReturn(gscObjectCreator.getMstOrderSiteStatus());
        when(userInfoUtils.getCustomerDetails()).thenReturn(gscObjectCreator.getCustomerList());
        mock(UserInformation.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .thenReturn(gscObjectCreator.getUserInformation());
        Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any())).thenReturn("");
    }

    /**
     * test Get Order by Id
     */
    @Test
    public void testGetGscOrderById() {
        Mockito.when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderToLeProductFamily());
        ResponseEntity response = gscOrderController.getGscOrderById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * test Get Order with simple Attributes by Id
     */
    @Test
    public void testGetGscOrderByIdSimpleAttribute() {
        Mockito.when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderToLeProductFamily());
        Mockito.when(orderProductComponentsAttributeValueRepository.findByOrderProductComponent(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValueList());
        ResponseEntity response = gscOrderController.getGscOrderById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * test Update Order With Array Of Attributes
     */
    @Test
    public void testUpdateOrderArrayAttributes() {
        Mockito.when(orderProductComponentsAttributeValueRepository.saveAll(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValueLists());
        GscApiRequest<List<GscOrderProductComponentBean>> request = new GscApiRequest<>();
        request.setAction(GscConstants.ACTION_UPDATE);
        List<GscOrderProductComponentBean> gscOrderProductComponentBean = GscTestUtil.fromJsonFile(
                "com/tcl/dias/oms/gsc/controller/update_order_attributes_001.json",
                new TypeReference<List<GscOrderProductComponentBean>>() {
                });
        request.setData(gscOrderProductComponentBean);

        ResponseEntity response = gscOrderController.updateOrderProductComponentAttributes(1, 1, 1, 1, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * test Update Order With Simple Type Attributes
     */
    @Test
    public void testUpdateOrderSimpleAttributes() {
        Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValue());
        GscApiRequest<List<GscOrderProductComponentBean>> request = new GscApiRequest<>();
        request.setAction(GscConstants.ACTION_UPDATE);
        List<GscOrderProductComponentBean> gscOrderProductComponentBean = GscTestUtil.fromJsonFile(
                "com/tcl/dias/oms/gsc/controller/update_order_attributes_002.json",
                new TypeReference<List<GscOrderProductComponentBean>>() {
                });
        request.setData(gscOrderProductComponentBean);

        ResponseEntity response = gscOrderController.updateOrderProductComponentAttributes(1, 1, 1, 1, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * test Update Order With TFN Attributes
     */
    @Test
    public void testUpdateOrderTfnAttributes() {
        Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValue());
        Mockito.when(orderGscTfnRepository.saveAll(Mockito.anyIterable())).thenReturn(ImmutableList.of());
        GscApiRequest<List<GscOrderProductComponentBean>> request = new GscApiRequest<>();
        request.setAction(GscConstants.ACTION_UPDATE);
        List<GscOrderProductComponentBean> gscOrderProductComponentBean = GscTestUtil.fromJsonFile(
                "com/tcl/dias/oms/gsc/controller/update_order_attributes_003.json",
                new TypeReference<List<GscOrderProductComponentBean>>() {
                });
        request.setData(gscOrderProductComponentBean);

        ResponseEntity response = gscOrderController.updateOrderProductComponentAttributes(1, 1, 1, 1, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateOrderTfnAttributesShouldFailIfBusinessInputsAreNull() {
        GscApiRequest<List<GscOrderProductComponentBean>> request = new GscApiRequest<>();
        request.setAction(GscConstants.ACTION_UPDATE);
        request.setData(null);

        ResponseEntity response = gscOrderController.updateOrderProductComponentAttributes(null, 1, 1, 1, request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        response = gscOrderController.updateOrderProductComponentAttributes(1, null, 1, 1, request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        response = gscOrderController.updateOrderProductComponentAttributes(1, 1, null, 1, request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        response = gscOrderController.updateOrderProductComponentAttributes(1, 1, 1, null, request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        response = gscOrderController.updateOrderProductComponentAttributes(1, 1, 1, null, request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * Positive test case for
     * {@link GscOrderController#getOrderLeAttributes(Integer, Integer)}
     */
    @Test
    public void testgetOrderLeAttributes() {
        ResponseEntity response = gscOrderController.getOrderLeAttributes(1, 1);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    /**
     * Positive test case for
     * {@link GscOrderController#getOrderLeAttributes(Integer, Integer)}
     */
    @Test
    public void testgetOrderLeAttributesForArrayValues() {
        Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrdersLeAttributeArrayValueList());
        ResponseEntity response = gscOrderController.getOrderLeAttributes(1, 1);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    /**
     * Negative test case for
     * {@link GscOrderController#getOrderLeAttributes(Integer, Integer)}
     */
    @Test
    public void testgetOrderLeAttributesForNullAttrValues() {
        Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any())).thenReturn(null);
        Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(null);
        Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
                .thenReturn(null);
        ResponseEntity response = gscOrderController.getOrderLeAttributes(1, 1);
        assertNotEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Positive test case for
     * {@link GscOrderController#saveOrderLeAttributes(Integer, Integer, GscApiRequest)}
     */
    @Test
    public void testsaveOrderLeAttributes() {
        GscOrderAttributesBean bean = GscTestUtil.fromJsonFile(
                "com/tcl/dias/oms/gsc/controller/update_order_le_attributes.json",
                new TypeReference<GscOrderAttributesBean>() {
                });
        GscApiRequest<GscOrderAttributesBean> request = new GscApiRequest<>();
        request.setAction("UPDATE");
        request.setData(bean);
        ResponseEntity response = gscOrderController.saveOrderLeAttributes(1, 1, request);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    /**
     * Negative test case for
     * {@link GscOrderController#saveOrderLeAttributes(Integer, Integer, GscApiRequest)}
     */
    @Test
    public void testsaveOrderLeAttributesForNull() {
        GscApiRequest<GscOrderAttributesBean> request = new GscApiRequest<>();
        request.setAction("UPDATE");
        request.setData(new GscOrderAttributesBean());
        ResponseEntity response = gscOrderController.saveOrderLeAttributes(1, 1, request);
        assertNotEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetOrderConfigurationById() {
        ResponseEntity response = gscOrderController.getOrderConfigurationDetails(1, 1, 1, 1, true);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetOrderConfigurationByIdWithoutAttributes() {
        ResponseEntity response = gscOrderController.getOrderConfigurationDetails(1, 1, 1, 1, false);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Positive test case for
     * {@link GscOrderController#saveOrderAttributes(Integer, GscApiRequest)}
     */
    @Test
    public void testSaveOrderAttributes() {
        GscApiRequest<GscOrderAttributesBean> request = new GscApiRequest<>();
        GscOrderAttributesBean attributesBean = GscTestUtil.fromJsonFile(
                "com/tcl/dias/oms/gsc/controller/save_order_level_attributes.json",
                new TypeReference<GscOrderAttributesBean>() {
                });
        request.setAction("UPDATE");
        request.setData(attributesBean);
        ResponseEntity response = gscOrderController.saveOrderAttributes(1, request);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    /**
     * Negative test case for
     * {@link GscOrderController#saveOrderAttributes(Integer, GscApiRequest)}
     */
    @Test
    public void testSaveOrderAttributesforNullOrderId() {
        GscApiRequest<GscOrderAttributesBean> request = new GscApiRequest<>();
        GscOrderAttributesBean attributesBean = GscTestUtil.fromJsonFile(
                "com/tcl/dias/oms/gsc/controller/save_order_level_attributes.json",
                new TypeReference<GscOrderAttributesBean>() {
                });
        request.setAction("UPDATE");
        request.setData(attributesBean);
        ResponseEntity response = gscOrderController.saveOrderAttributes(null, request);
        assertNotEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Negative test case for
     * {@link GscOrderController#saveOrderAttributes(Integer, GscApiRequest)}
     */
    @Test
    public void testSaveOrderAttributesforNullAttributes() {
        GscApiRequest<GscOrderAttributesBean> request = new GscApiRequest<>();
        GscOrderAttributesBean attributesBean = null;
        request.setAction("UPDATE");
        request.setData(attributesBean);
        ResponseEntity response = gscOrderController.saveOrderAttributes(1, request);
        assertNotEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetOrderAttributes() {
        ResponseEntity response = gscOrderController.getOrderAttributes(1);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetOrderAttributesForNullOrderId() {
        ResponseEntity response = gscOrderController.getOrderAttributes(null);
        assertNotEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testUpdateOrderConfigurationDetailsShouldFailWithInvalidInputs() {
        GscApiRequest<List<GscOrderConfigurationBean>> apiRequest = new GscApiRequest<>();
        apiRequest.setAction(GscConstants.ACTION_UPDATE);
        ResponseEntity responseEntity = gscOrderController.updateOrderConfigurationDetails(null, 1, 1, apiRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        gscOrderController.updateOrderConfigurationDetails(1, null, 1, apiRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        gscOrderController.updateOrderConfigurationDetails(1, 1, null, apiRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        gscOrderController.updateOrderConfigurationDetails(1, 1, 1, apiRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateOrderConfigurationDetailsShouldBeSuccessWithDefaultInputs() {
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
        GscApiRequest<List<GscOrderConfigurationBean>> apiRequest = new GscApiRequest<>();
        apiRequest.setAction(GscConstants.ACTION_UPDATE);
        apiRequest.setData(configurations);
        ResponseEntity responseEntity = gscOrderController.updateOrderConfigurationDetails(1, 1, 1, apiRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAvailableNumbersShouldFailWithInvalidInputs() {
        ResponseEntity response = gscOrderController.getAvailableNumbers(null, null, 10, null);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        response = gscOrderController.getAvailableNumbers(10, null, null, null);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        response = gscOrderController.getAvailableNumbers(10, null, 10, null);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetAvailableNumbersShouldSucceedWithValidInputs() throws TclCommonException {
        OrderGscDetail orderGscDetail = gscObjectCreator.getOrderGscDetail();
        orderGscDetail.setOrderGsc(gscObjectCreator.getordergsc());
        orderGscDetail.getOrderGsc().setOrderToLe(gscObjectCreator.getOrderToLe());
        orderGscDetail.setOrderGscTfns(new HashSet<>());
        Mockito.when(orderGscDetailRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(orderGscDetail));
        Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("{\"accounCuId\":\"1\"}");
        Mockito.when(orderGscTfnRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

        ResponseEntity response = gscOrderController.getAvailableNumbers(1, null, 10, true);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSaveNumbersShouldFailWithInvalidInputs() {
        ResponseEntity response = gscOrderController.saveNumbers(null, ImmutableList.of());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        response = gscOrderController.saveNumbers(null, null);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSaveNumbersShouldSucceedWithValidInputs() throws TclCommonException {
        OrderGscDetail orderGscDetail = gscObjectCreator.getOrderGscDetail();
        orderGscDetail.setOrderGsc(gscObjectCreator.getordergsc());
        orderGscDetail.getOrderGsc().setOrderToLe(gscObjectCreator.getOrderToLe());
        orderGscDetail.setOrderGscTfns(new HashSet<>());
        Mockito.when(orderGscDetailRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(orderGscDetail));
        Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("{\"accounCuId\":\"1\"}");
        Mockito.when(orderGscTfnRepository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
        List<GscTfnBean> tfns = GscTestUtil.fromJsonFile(
                "com/tcl/dias/oms/gsc/controller/save_configuration_numbers_001.json",
                new TypeReference<List<GscTfnBean>>() {
                });
        ResponseEntity response = gscOrderController.saveNumbers(1, tfns);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateOrderProductComponentAttributesForSolutionsShouldFailForInvalidInputs() {
        ResponseEntity response = gscOrderController.updateOrderProductComponentAttributesForSolutions(null,
                new GscApiRequest<>());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        response = gscOrderController.updateOrderProductComponentAttributesForSolutions(1, null);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateOrderProductComponentAttributesForSolutionsShouldSucceedForValidInputs() {
        Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
                .thenReturn(gscObjectCreator.getProductAttributeMasterList());
        Mockito.when(orderProductComponentsAttributeValueRepository
                .findByOrderProductComponentAndProductAttributeMaster(Mockito.any(), Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValueList());
        Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
                .then(AdditionalAnswers.returnsFirstArg());
        GscApiRequest<List<GscOrderSolutionBean>> request = GscTestUtil.fromJsonFile(
                "com/tcl/dias/oms/gsc/controller/bulk_update_configuration_001.json",
                new TypeReference<GscApiRequest<List<GscOrderSolutionBean>>>() {
                });
        ResponseEntity response = gscOrderController.updateOrderProductComponentAttributesForSolutions(1, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
