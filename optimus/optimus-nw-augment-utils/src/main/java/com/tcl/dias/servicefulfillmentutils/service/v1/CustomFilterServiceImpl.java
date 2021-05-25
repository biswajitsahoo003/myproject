package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.networkaugment.entity.entities.CustomFilter;
import com.tcl.dias.networkaugment.entity.repository.CustomFilterRepository;
import com.tcl.dias.networkaugment.entity.repository.MstTaskDefRepository;
import com.tcl.dias.servicefulfillmentutils.beans.CustomFilterBean;
import com.tcl.dias.servicefulfillmentutils.beans.FilterBean;
import com.tcl.dias.servicefulfillmentutils.beans.FilterTypeBean;
import com.tcl.dias.servicefulfillmentutils.beans.MstTaskDefBean;
import com.tcl.dias.servicefulfillmentutils.beans.Preference;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class CustomFilterServiceImpl implements CustomFilterService {
    public static final byte SET_DEFAULT_TO_INACTIVE = (byte) 0;
    private static final Logger log = LoggerFactory.getLogger(CustomFilterServiceImpl.class);
    @Autowired
    CustomFilterRepository customFilterRepository;

    @Autowired
    MstTaskDefRepository mstTaskDefRepository;
    
    @Autowired
    UserInfoUtils userInfoUtils;

    @Override
	public CustomFilterBean getCustomFilterDetails(String user, String group, String type) {
		CustomFilterBean customFilterBean = new CustomFilterBean();

		String userName = userInfoUtils.getUserInformation().getUserId();
		//if(type!=null && "alltask".equals(type))userName="";

		List<FilterTypeBean> filterFilterTypeBeans = new ArrayList<>();
		List<CustomFilter> customFilters = new ArrayList<>();
		if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(group))
			customFilters = customFilterRepository.findByUserNameAndGroupNameAndType(userName, group, type);
		else if (!StringUtils.isEmpty(userName))
			customFilters = customFilterRepository.findByUserNameAndType(userName, type);
		else if (!StringUtils.isEmpty(group))
			customFilters = customFilterRepository.findByGroupNameAndType(group, type);
		if (CollectionUtils.isEmpty(customFilters))
			return new CustomFilterBean(userName);
		customFilters.stream().collect(Collectors.groupingBy(CustomFilter::getType)).entrySet().forEach(entrySet -> {
			FilterTypeBean filterTypeBean = new FilterTypeBean();
			filterTypeBean.setType(entrySet.getKey());
			filterTypeBean.setFilters(setCustomFilters(entrySet.getValue()));
			filterFilterTypeBeans.add(filterTypeBean);
		});
		customFilterBean.setUsername(userName);
		customFilterBean.setGroupName(group);
		customFilterBean.setFilterTypes(filterFilterTypeBeans);
		return customFilterBean;
	}

    private List<FilterBean> setCustomFilters(List<CustomFilter> customFilters) {
        List<FilterBean> filterBeans = new ArrayList<>();
        customFilters
                .forEach(customFilter -> {
                    FilterBean filterBean = new FilterBean();
                    filterBean.setFilterName(customFilter.getFilterName());
                    filterBean.setIsDefault(customFilter.getIsDefault());
                    try {
                        filterBean.setPreference(Utils.convertJsonToObject(customFilter.getPreference(), Preference.class));
                    } catch (Exception e) {
                        log.error("Exception occurred while converting filter preference to json --> {}", e);
                    }
                    filterBeans.add(filterBean);
                });
        return filterBeans;
    }

    @Override
    @Transactional
    public CustomFilterBean saveCustomFilterDetails(CustomFilterBean customFilterBean) {
        customFilterBean.getFilterTypes()
                .forEach(filterType -> {
                    String type = filterType.getType();
                    filterType.getFilters()
                            .forEach(filter -> {
                                if (!filter.getIsDefault().equals(SET_DEFAULT_TO_INACTIVE)) {
                                    Optional<CustomFilter> defaultFilter = customFilterRepository.findByUserNameAndGroupNameAndTypeAndIsDefault(customFilterBean.getUsername(), customFilterBean.getUsername(), type, (byte) 1);
                                    if (defaultFilter.isPresent()) {
                                        CustomFilter defaultCustomFilter = defaultFilter.get();
                                        defaultCustomFilter.setIsDefault(SET_DEFAULT_TO_INACTIVE);
                                        customFilterRepository.saveAndFlush(defaultCustomFilter);
                                    }
                                }
                                CustomFilter customFilter = new CustomFilter();
                                customFilter.setUserName(customFilterBean.getUsername());
                                customFilter.setType(type);
                                customFilter.setGroupName(customFilterBean.getGroupName());
                                customFilter.setFilterName(filter.getFilterName());
                                customFilter.setIsDefault(filter.getIsDefault());
                                try {
                                    customFilter.setPreference(Utils.convertObjectToJson(filter.getPreference()));
                                } catch (TclCommonException e) {
                                    log.error("Exception occurred while converting filter preference to json --> {}", e);
                                }
                                customFilterRepository.save(customFilter);
                            });
                });
        return customFilterBean;
    }

    @Override
    public List<MstTaskDefBean> getMstTasksByAssignedGroup(String[] assignedGroup) {
        return mstTaskDefRepository.findByAssignedGroupIn(Arrays.asList(assignedGroup))
                .stream()
                .map(MstTaskDefBean::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCustomFilter(String filterName, String filterType, String groupName) {
        customFilterRepository.findFirstByUserNameAndFilterNameAndTypeAndGroupName(userInfoUtils.getUserInformation().getUserId(), filterName, filterType, groupName)
                .ifPresent(filter -> customFilterRepository.delete(filter));
    }
}
