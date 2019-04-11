package com.mettre.security.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 权限集合
 */
@Data
public class JsonPermissions {

    private List<SimplePermission> permissions;

    @Data
    public static class SimplePermission {

        /**
         * 资源id
         */
        private String resourceId;

        /**
         * 资源名
         */
        private String resourceName;

        /**
         * 权限列表
         */
        private Map<String, String> privileges;

        /**
         * 是否被遗弃
         */
        private boolean abandon = false;
    }
}
