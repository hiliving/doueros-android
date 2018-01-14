package com.baidu.duer.dcs.devicemodule.audioplayer.domain;

/**
 * Created by Huangyong on 2018/1/14.
 */

public class ContentInfo {

    /**
     * header : {"namespace":"ai.dueros.device_interface.screen","name":"HtmlView","messageId":"NWE1YTMwYzkzN2ExNw==","dialogRequestId":"11010eb6-ed9f-498b-9bf1-02c0f328fc21"}
     * payload : {"url":"http://xiaodu.baidu.com/saiya/dcsview?id=b0908f6d7668db27a0e78c9fa5bfc6df&appid=dmE692434811AEF3AE","token":"eyJib3RfaWQiOiJ1cyIsInJlc3VsdF90b2tlbiI6IjViNWQ0ODZmNTBkY2E1NjZkYjE5ODNkY2E3ZWFiOWNiIiwiYm90X3Rva2VuIjoibnVsbCJ9"}
     */

    private HeaderBean header;
    private PayloadBean payload;

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public PayloadBean getPayload() {
        return payload;
    }

    public void setPayload(PayloadBean payload) {
        this.payload = payload;
    }

    public static class HeaderBean {
        /**
         * namespace : ai.dueros.device_interface.screen
         * name : HtmlView
         * messageId : NWE1YTMwYzkzN2ExNw==
         * dialogRequestId : 11010eb6-ed9f-498b-9bf1-02c0f328fc21
         */

        private String namespace;
        private String name;
        private String messageId;
        private String dialogRequestId;

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getDialogRequestId() {
            return dialogRequestId;
        }

        public void setDialogRequestId(String dialogRequestId) {
            this.dialogRequestId = dialogRequestId;
        }
    }

    public static class PayloadBean {
        /**
         * url : http://xiaodu.baidu.com/saiya/dcsview?id=b0908f6d7668db27a0e78c9fa5bfc6df&appid=dmE692434811AEF3AE
         * token : eyJib3RfaWQiOiJ1cyIsInJlc3VsdF90b2tlbiI6IjViNWQ0ODZmNTBkY2E1NjZkYjE5ODNkY2E3ZWFiOWNiIiwiYm90X3Rva2VuIjoibnVsbCJ9
         */

        private String url;
        private String token;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
