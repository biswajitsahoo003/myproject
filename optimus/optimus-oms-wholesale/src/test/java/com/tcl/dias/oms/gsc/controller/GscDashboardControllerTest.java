package com.tcl.dias.oms.gsc.controller;

import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
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
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.controller.v1.GscDashboardController;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.utils.GscObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;

/**
 * Contains all test cases of GscDashBoard Controller
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GscDashboardControllerTest {

    @Autowired
    GscObjectCreator gscObjectCreator;
    /*@Autowired
    ObjectCreator omsObjectCreator;*/
    @Autowired
    GscDashboardController gscDashboardController;
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
    UserInfoUtils userInfoUtils;

    @MockBean
    MstOrderSiteStageRepository mstOrderSiteStageRepository;

    @MockBean
    MstOrderSiteStatusRepository mstOrderSiteStatusRepository;

    @MockBean
    MstProductComponentRepository mstProductComponentRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    NotificationService notificationService;

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
        Mockito.when(orderToLeRepository.findByOrder(Mockito.any())).thenReturn(gscObjectCreator.getOrderToLesList());
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
                .thenReturn(gscObjectCreator.getOrdersLeAttributeValue());
        doNothing().when(ordersLeAttributeValueRepository).deleteAll();
        Mockito.when(ordersLeAttributeValueRepository.saveAll(Mockito.any()))
                .thenReturn(new ArrayList<OrdersLeAttributeValue>());

        Mockito.when(mstOrderSiteStageRepository.findByName(Mockito.anyString()))
                .thenReturn(gscObjectCreator.getMstOrderSiteStage());
        Mockito.when(mstOrderSiteStatusRepository.findByName(Mockito.anyString()))
                .thenReturn(gscObjectCreator.getMstOrderSiteStatus());
        Mockito.when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductFamily());
        Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString())).
                thenReturn(gscObjectCreator.getMstProductComponent());
        Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponent(Mockito.anyInt(), Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentLists());
        Mockito.when(orderProductComponentRepository.save(Mockito.any())).thenReturn(gscObjectCreator.getOrderProductComponent());
    }

    /**
     * test Get Order by Id
     */
    @Test
    public void testGetGscOrderById() {
        ResponseEntity response = gscDashboardController.getOrderDetails(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * test Get Order with simple Attributes by Id
     */
    @Test
    public void testGetGscOrderByIdSimpleAttribute() {
        Mockito.when(orderProductComponentsAttributeValueRepository.findByOrderProductComponent(Mockito.any()))
                .thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValueList());
        ResponseEntity response = gscDashboardController.getOrderDetails(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * test DashBoard API
     */
    @Test
    public void testDashboard() throws TclCommonException {
        Mockito.when(userInfoUtils.getCustomerDetails()).thenReturn(gscObjectCreator.getCustomerList());
        Mockito.when(orderToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.anyInt()))
                .thenReturn(gscObjectCreator.getOrderToLeList());
        Mockito.when(orderGscRepository.findByorderProductSolution(Mockito.any()))
                .thenReturn(gscObjectCreator.getGscOrderList());
        ResponseEntity response = gscDashboardController.getDashboardDetails();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSendMailNotification() throws TclCommonException {
        Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(gscObjectCreator.getUser());
        Mockito.when(notificationService.helpTicketNotification(Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        gscDashboardController.sendMailNotification();
        assert (true);
    }

}
