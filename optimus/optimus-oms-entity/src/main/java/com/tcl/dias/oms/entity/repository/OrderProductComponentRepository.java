package com.tcl.dias.oms.entity.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;

/**
 * This file contains the OrderProductComponentRepository.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface OrderProductComponentRepository extends JpaRepository<OrderProductComponent, Integer> {

	List<OrderProductComponent> findByReferenceIdAndMstProductComponent(Integer siteId,MstProductComponent mstProductComponent);

	List<OrderProductComponent> findByReferenceIdInAndReferenceName(Integer id, String referenceName);
	
	List<OrderProductComponent> findByReferenceIdInAndMstProductComponent(List<Integer> refIds,MstProductComponent mstProductComponent);

	
	List<OrderProductComponent> findByReferenceIdAndMstProductComponentAndType(Integer siteId,MstProductComponent mstProductComponent,String type);
	
	List<OrderProductComponent> findByReferenceIdAndType(Integer referenceId,String type);
	
	List<OrderProductComponent> findByReferenceIdAndTypeAndReferenceName(Integer referenceId,String type,String referenceName);
	
	List<OrderProductComponent> findByReferenceIdAndMstProductComponent_NameAndReferenceName(Integer siteId,String mstName,String referenceName);

	List<OrderProductComponent> findByReferenceIdAndMstProductComponent_NameAndMstProductFamily(Integer refId, String mstName, MstProductFamily mstProductFamily);
	
	List<OrderProductComponent> findByReferenceId(Integer siteId);
	
	List<OrderProductComponent> findByReferenceIdAndMstProductComponent_NameAndType(Integer siteId,String cmpName, String type);
	List<OrderProductComponent> findByReferenceIdAndMstProductFamily_Name(Integer siteId, String prodName);
	//List<OrderProductComponent> findByReferenceIdAndMstProductFamily_NameAndReferenceName(Integer siteId, String prodName,String referenceName);
	List<OrderProductComponent> findByReferenceIdAndTypeAndMstProductFamily_Name(Integer referenceId,String type, String productFamilyName);

	/**
	 * @author DimpleS
	 * 
	 * findByRefidAndRefName is used to fetch order product component by cloud reference and name
	 * this will used in the IPC MACD Order summary screen.
	 *
	 * @param refId
	 * @param refName
	 * @return OrderProductComponent
	 */
	@Query(value = "SELECT op.reference_name as item_type,mpc.name as item,op.effective_nrc as nrc,op.effective_mrc as mrc,op.effective_arc as arc FROM order_price op, order_product_component opc, mst_product_component mpc WHERE op.reference_id=opc.id AND mpc.id=opc.product_component_id AND opc.reference_id=:refId and opc.reference_name=:refName AND opc.type IS NULL", nativeQuery = true)
	List<Map<String, Object>> findByRefidAndRefName(@Param("refId") String refId, @Param("refName") String refName);

	@Query(value = "SELECT opc.id as id,op.reference_name as item_type,mpc.name as item,op.effective_nrc as nrc,op.effective_mrc as mrc,op.effective_arc as arc FROM order_price op, order_product_component opc, mst_product_component mpc WHERE op.reference_id=opc.id AND mpc.id=opc.product_component_id AND opc.reference_id=:refId AND op.quote_id=:quoteId AND opc.reference_name=:refName AND opc.type IS NULL", nativeQuery = true)
	List<Map<String, Object>> findByRefIdAndRefNameAndQuoteId(@Param("refId") String refId, @Param("refName") String refName, @Param("quoteId") Integer quoteId);
	
	List<OrderProductComponent> findByReferenceIdAndMstProductFamily_NameAndReferenceName(Integer siteId, String prodName, String referenceName);

	List<OrderProductComponent> findByReferenceIdAndReferenceName(Integer referenceId,String referenceName);
	
	List<OrderProductComponent> findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(Integer id, MstProductComponent  mstProductComponent, MstProductFamily mstProductFamily, String type ); 

	List<OrderProductComponent> findByReferenceIdAndMstProductComponentAndMstProductFamilyAndTypeAndReferenceName(Integer id, 
			MstProductComponent  mstProductComponent, MstProductFamily mstProductFamily, String type,String refName);

	@Query(value = "select opc.* from order_cloud oc join order_product_component opc on oc.id = opc.reference_id and oc.cloud_code =:cloudCode and opc.product_component_id =:componentId", nativeQuery = true)
	OrderProductComponent findByCloudCodeAndMstProductComponent(String cloudCode, Integer componentId); 

	@Query(value="select opc.* from order_cloud oc join order_product_component opc on oc.id=opc.reference_id and oc.cloud_code =:cloudCode and opc.product_component_id =:componentId and opc.product_family_id =:productFamilyId",nativeQuery=true)
	OrderProductComponent findByParentCloudCodeAndMstProductComponentIdAndMstProductFamilyId(String cloudCode , Integer componentId, Integer productFamilyId);
	
	List<OrderProductComponent> findByReferenceIdAndAndMstProductFamilyAndReferenceName(Integer id, MstProductFamily mstProductFamily,String referenceName);

}
