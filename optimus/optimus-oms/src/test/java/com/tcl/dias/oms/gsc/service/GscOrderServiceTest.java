package com.tcl.dias.oms.gsc.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableList;
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
import com.tcl.dias.oms.gsc.GscTestUtil;
import com.tcl.dias.oms.gsc.beans.GscApiRequest;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderSolutionBean;
import com.tcl.dias.oms.gsc.beans.GscOrderStatusStageUpdate;
import com.tcl.dias.oms.gsc.pdf.beans.GscOrderAttributesBean;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.utils.GscObjectCreator;
import com.tcl.dias.oms.utils.ObjectCreator;

import io.vavr.control.Try;

/**
 * THis class contains all the test cases related to
 * {@link com.tcl.dias.oms.gsc.service.v1.GscOrderService}
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GscOrderServiceTest {

	@Autowired
	GscOrderService gscOrderService;

	@Autowired
	GscObjectCreator gscObjectCreator;

	@Autowired
	ObjectCreator omsObjectCreator;

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
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@MockBean
	MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	MstOrderSiteStatusRepository mstOrderSiteStatusRepository;

	@MockBean
	MstOrderSiteStageRepository mstOrderSiteStageRepository;

	@Before
	public void init() {

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
		Mockito.when(orderToLeRepository.findByOrder(Mockito.any())).thenReturn(omsObjectCreator.getOrderToLesList());
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLe_Id(Mockito.any()))
				.thenReturn(omsObjectCreator.getorderToLeProductFamilies());
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
				.thenReturn(Optional.of(omsObjectCreator.getOrderToLes()));
		Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(omsObjectCreator.getOrdersLeAttributeValueList());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(gscObjectCreator.getMstProductFamily());
		Mockito.when(orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster(Mockito.any(), Mockito.any()))
				.thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValueList());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(gscObjectCreator.getProductAtrributeMaster());
		Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(omsObjectCreator.createOrderProducts());
		Mockito.when(mstOrderSiteStatusRepository.findByName(Mockito.anyString()))
				.thenReturn(omsObjectCreator.getMstOrderSiteStatus());
		Mockito.when(mstOrderSiteStageRepository.findByName(Mockito.anyString()))
				.thenReturn(omsObjectCreator.getMstOrderSiteStage());
	}

	/**
	 *
	 * test Update Order With Array Of Attributes
	 */
	@Test
	public void testUpdateOrderArrayAttributes() {
		Mockito.when(orderProductComponentsAttributeValueRepository.saveAll(Mockito.any()))
				.thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValueLists());
		List<GscOrderProductComponentBean> gscOrderProductComponentBean = GscTestUtil.fromJsonFile(
				"com/tcl/dias/oms/gsc/controller/update_order_attributes_001.json",
				new TypeReference<List<GscOrderProductComponentBean>>() {
				});

		Try<?> result = Try.of(
				() -> gscOrderService.updateOrderProductComponentAttributes(1, 1, 1, 1, gscOrderProductComponentBean));
		result.onFailure(Throwable::printStackTrace);
		assertTrue(result.isSuccess());
	}

	/**
	 *
	 * test Update Order With Simple Type Attributes
	 */
	@Test
	public void testUpdateOrderSimpleAttributes() {
		Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValue());
		List<GscOrderProductComponentBean> gscOrderProductComponentBean = GscTestUtil.fromJsonFile(
				"com/tcl/dias/oms/gsc/controller/update_order_attributes_002.json",
				new TypeReference<List<GscOrderProductComponentBean>>() {
				});

		Try<?> result = Try.of(
				() -> gscOrderService.updateOrderProductComponentAttributes(1, 1, 1, 1, gscOrderProductComponentBean));
		result.onFailure(Throwable::printStackTrace);
		assertTrue(result.isSuccess());
	}

	/**
	 *
	 * test Update Order With TFN Attributes
	 */
	@Test
	public void testUpdateOrderTfnAttributes() {
		Mockito.when(orderProductComponentsAttributeValueRepository.saveAll(Mockito.any()))
				.thenReturn(gscObjectCreator.getOrderProductComponentsAttributeValueLists());
		List<GscOrderProductComponentBean> gscOrderProductComponentBean = GscTestUtil.fromJsonFile(
				"com/tcl/dias/oms/gsc/controller/update_order_attributes_003.json",
				new TypeReference<List<GscOrderProductComponentBean>>() {
				});

		Try<?> result = Try.of(
				() -> gscOrderService.updateOrderProductComponentAttributes(1, 1, 1, 1, gscOrderProductComponentBean));
		result.onFailure(Throwable::printStackTrace);
		assertTrue(result.isSuccess());
	}

	@Test
	public void testUpdateOrderTfnAttributesShouldFailIfBusinessInputsAreNull() {
		Try<?> result = Try.of(() -> gscOrderService.updateOrderProductComponentAttributes(null, 1, 1, 1, null));
		assertTrue(result.isFailure());
		result = Try.of(() -> gscOrderService.updateOrderProductComponentAttributes(1, null, 1, 1, null));
		assertTrue(result.isFailure());
		result = Try.of(() -> gscOrderService.updateOrderProductComponentAttributes(1, 1, null, 1, null));
		assertTrue(result.isFailure());
		result = Try.of(() -> gscOrderService.updateOrderProductComponentAttributes(1, 1, 1, null, null));
		assertTrue(result.isFailure());
		result = Try.of(() -> gscOrderService.updateOrderProductComponentAttributes(1, 1, 1, null, null));
		assertTrue(result.isFailure());
		result = Try.of(() -> gscOrderService.updateOrderProductComponentAttributes(1, 1, 1, 1, null));
		assertTrue(result.isFailure());
	}

	/**
	 * Positive test case for
	 * {@link GscOrderService#getOrderToLeAttributes(Integer, Integer)}
	 */
	@Test
	public void testgetOrderToLeAttributes() {
		GscOrderAttributesBean response = gscOrderService.getOrderToLeAttributes(1, 1);
		assertNotNull(response);
	}

	/**
	 * Negative test case for
	 * {@link GscOrderService#getOrderToLeAttributes(Integer, Integer)}
	 */
	@Test(expected = NullPointerException.class)
	public void testgetOrderToLeAttributesForNull() {
		GscOrderAttributesBean response = gscOrderService.getOrderToLeAttributes(null, null);
	}

	@Test
	public void testGetOrderArributes() {
		GscOrderAttributesBean response = gscOrderService.getOrderAttributes(1);
		assertNotNull(response.getAttributes());
	}

	@Test(expected = NullPointerException.class)
	public void testGetOrderAttributesForNullOrderId() {
		GscOrderAttributesBean response = gscOrderService.getOrderAttributes(null);
	}

	@Test
	public void testSaveOrderAttributes() {
		GscOrderAttributesBean attributesBean = GscTestUtil.fromJsonFile(
				"com/tcl/dias/oms/gsc/controller/save_order_level_attributes.json",
				new TypeReference<GscOrderAttributesBean>() {
				});
		List<GscOrderProductComponentsAttributeValueBean> response = gscOrderService.saveOrderAttributes(1,
				attributesBean.getAttributes());
		assertNotNull(response);
	}

	@Test(expected = NullPointerException.class)
	public void testSaveOrderAttributeForNullOrderId() {
		GscOrderAttributesBean attributesBean = GscTestUtil.fromJsonFile(
				"com/tcl/dias/oms/gsc/controller/save_order_level_attributes.json",
				new TypeReference<GscOrderAttributesBean>() {
				});
		List<GscOrderProductComponentsAttributeValueBean> response = gscOrderService.saveOrderAttributes(null,
				attributesBean.getAttributes());
	}

	@Test(expected = NullPointerException.class)
	public void testSaveOrderAttributesForNullAttributes() {
		List<GscOrderProductComponentsAttributeValueBean> response = gscOrderService.saveOrderAttributes(1, null);
	}

	@Test
	public void testupdateOrderConfigurationStageStatus() {
		GscOrderStatusStageUpdate gscOrderStatusStageUpdate = GscTestUtil.fromJsonFile(
				"com/tcl/dias/oms/gsc/controller/update_order_status_stage.json",
				new TypeReference<GscOrderStatusStageUpdate>() {
				});
		GscOrderStatusStageUpdate response = gscOrderService.updateOrderConfigurationStageStatus(1,
				gscOrderStatusStageUpdate);
		assertNotNull(response);
	}

	@Test(expected = NullPointerException.class)
	public void testupdateOrderConfigurationStageStatusForNullConfigId() {
		GscOrderStatusStageUpdate gscOrderStatusStageUpdate = GscTestUtil.fromJsonFile(
				"com/tcl/dias/oms/gsc/controller/update_order_status_stage.json",
				new TypeReference<GscOrderStatusStageUpdate>() {
				});
		gscOrderService.updateOrderConfigurationStageStatus(null, gscOrderStatusStageUpdate);
	}

	@Test
	public void testUpdateProductComponentAttributesForSolutionsShouldFailForInvalidInputs() {
		Try<?> result = Try
				.of(() -> gscOrderService.updateProductComponentAttributesForSolutions(null, ImmutableList.of()));
		assertTrue(result.isFailure());
		result = Try.of(() -> gscOrderService.updateProductComponentAttributesForSolutions(1, ImmutableList.of()));
		assertTrue(result.isFailure());
	}

	@Test
	public void testUpdateProductComponentAttributesForSolutionsShouldSucceedForValidInputs() {
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
		Try<?> result = Try
				.of(() -> gscOrderService.updateProductComponentAttributesForSolutions(1, request.getData()));
		result.onFailure(Throwable::printStackTrace);
		assertTrue(result.isSuccess());
	}
}
