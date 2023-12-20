package com.jsl.oa.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;

/**
 * <h1>自定义快捷工具类</h1>
 * <hr/>
 *
 * @author 筱锋xiao_lfeng
 * @since v1.0.0
 * @version v1.0.0
 */
public class Processing {
    public static ArrayList<String> getValidatedErrorList(BindingResult bindingResult) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            arrayList.add(objectError.getDefaultMessage());
        }
        return arrayList;
    }
}
