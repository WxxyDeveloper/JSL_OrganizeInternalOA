package com.jsl.oa.dao;

import com.google.gson.Gson;
import com.jsl.oa.mapper.ModuleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModuleDAO {
    private final ModuleMapper moduleMapper;
    private final Gson gson;
}
