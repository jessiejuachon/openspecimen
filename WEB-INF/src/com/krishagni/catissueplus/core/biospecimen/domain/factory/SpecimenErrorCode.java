
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum SpecimenErrorCode implements ErrorCode {
	NOT_FOUND,
	
	INVALID_SPECIMEN_CLASS,
	
	INVALID_SPECIMEN_TYPE,
	
	INVALID_VISIT,
	
	VISIT_REQUIRED,
	
	COMPL_VISIT_REQ,
	
	COMPL_OR_PENDING_VISIT_REQ,
	
	COMPL_OR_MISSED_VISIT_REQ,
	
	LABEL_REQUIRED,
	
	DUP_LABEL,

	DUP_LABEL_IN_CP,
	
	INVALID_LABEL,
	
	MANUAL_LABEL_NOT_ALLOWED,
	
	DUP_BARCODE,
	
	INVALID_LINEAGE,
	
	INVALID_COLL_STATUS,
	
	INVALID_QTY,

	ALIQUOT_QTY_REQ,
	
	PARENT_REQUIRED,
	
	PARENT_NF_BY_VISIT_AND_SR,
	
	COLL_PARENT_REQ,
	
	COLL_OR_MISSED_PARENT_REQ,
	
	COLL_OR_PENDING_PARENT_REQ,
	
	SPECIMEN_CLASS_REQUIRED,
	
	SPECIMEN_TYPE_REQUIRED,
	
	INVALID_ANATOMIC_SITE,
	
	ANATOMIC_SITE_NOT_SAME_AS_PARENT,
	
	INVALID_LATERALITY,
	
	LATERALITY_NOT_SAME_AS_PARENT,
	
	INVALID_PATHOLOGY_STATUS,
	
	COLL_DATE_REQUIRED,
	
	INVALID_CREATION_DATE,

	CREATED_ON_GT_CURRENT,

	CHILD_CREATED_ON_LT_PARENT,

	PARENT_CREATED_ON_GT_CHILDREN,
	
	EDIT_NOT_ALLOWED,
	
	AVBL_QTY_GT_INIT_QTY,
	
	REF_ENTITY_FOUND,
	
	NO_PRINTER_CONFIGURED,
	
	NO_SPECIMENS_TO_PRINT,
	
	PRINT_ERROR,
	
	NOT_AVAILABLE_FOR_DIST,
	
	INVALID_BIOHAZARDS,
	
	INVALID_COLL_PROC,
	
	INVALID_COLL_CONTAINER,
	
	INVALID_RECV_QUALITY,
	
	INVALID_QTY_OR_CNT,

	ALIQUOT_CNT_N_QTY_REQ,

	NOT_COLLECTED,
	
	NO_POOL_SPMN_COLLECTED,
	
	NO_POOLED_SPMN,

	INVALID_FREEZE_THAW_CYCLES,

	UQ_LBL_CP_CHG_NA,

	INVALID_DISPOSE_STATUS,

	DISPOSAL_DT_LT_COLL_DT,

	DISPOSAL_DT_LT_CREATED_ON,

	CONTAINER_ACCESS_DENIED,

	PARENT_CONTAINER_REQ,

	CONTAINER_TYPE_REQ;

	public String code() {
		return "SPECIMEN_" + this.name();
	}
}
