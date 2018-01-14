package com.baidu.duer.dcs.devicemodule.audioplayer.domain;

/**
 * Created by Huangyong on 2018/1/14.
 */

public class MusicInfo {

    /**
     * header : {"messageId":"YXVkaW9fbXVzaWMrMTUxNTg1OTk5NV82NDQ0cjduc2U=","name":"Play","namespace":"ai.dueros.device_interface.audio_player","dialogRequestId":"808d250f-e241-4fb5-a762-080dd91bbda0"}
     * payload : {"audioItem":{"audioItemId":"1034728280","stream":{"expiryTime":"2018-01-14T00:43:15+08:00","offsetInMilliseconds":0,"progressReport":{"progressReportIntervalInMilliseconds":15000},"streamFormat":"AUDIO_MPEG","token":"eyJib3RfaWQiOiJhdWRpb19tdXNpYyIsInJlc3VsdF90b2tlbiI6ImNiZWZmMzM3ZDFmZTk5ZjkyNjlhNjQ2ZjcwZDEyMTFkIiwiYm90X3Rva2VuIjoiMTAzNDcyODI4MCJ9","url":"http://other.web.rh01.sycdn.kuwo.cn/98caf6bd4b1085483565aee8f1ec9f4d/5a5a301b/resource/n3/19/99/1188105613.mp3"}},"playBehavior":"REPLACE_ALL"}
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
         * messageId : YXVkaW9fbXVzaWMrMTUxNTg1OTk5NV82NDQ0cjduc2U=
         * name : Play
         * namespace : ai.dueros.device_interface.audio_player
         * dialogRequestId : 808d250f-e241-4fb5-a762-080dd91bbda0
         */

        private String messageId;
        private String name;
        private String namespace;
        private String dialogRequestId;

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
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
         * audioItem : {"audioItemId":"1034728280","stream":{"expiryTime":"2018-01-14T00:43:15+08:00","offsetInMilliseconds":0,"progressReport":{"progressReportIntervalInMilliseconds":15000},"streamFormat":"AUDIO_MPEG","token":"eyJib3RfaWQiOiJhdWRpb19tdXNpYyIsInJlc3VsdF90b2tlbiI6ImNiZWZmMzM3ZDFmZTk5ZjkyNjlhNjQ2ZjcwZDEyMTFkIiwiYm90X3Rva2VuIjoiMTAzNDcyODI4MCJ9","url":"http://other.web.rh01.sycdn.kuwo.cn/98caf6bd4b1085483565aee8f1ec9f4d/5a5a301b/resource/n3/19/99/1188105613.mp3"}}
         * playBehavior : REPLACE_ALL
         */

        private AudioItemBean audioItem;
        private String playBehavior;

        public AudioItemBean getAudioItem() {
            return audioItem;
        }

        public void setAudioItem(AudioItemBean audioItem) {
            this.audioItem = audioItem;
        }

        public String getPlayBehavior() {
            return playBehavior;
        }

        public void setPlayBehavior(String playBehavior) {
            this.playBehavior = playBehavior;
        }

        public static class AudioItemBean {
            /**
             * audioItemId : 1034728280
             * stream : {"expiryTime":"2018-01-14T00:43:15+08:00","offsetInMilliseconds":0,"progressReport":{"progressReportIntervalInMilliseconds":15000},"streamFormat":"AUDIO_MPEG","token":"eyJib3RfaWQiOiJhdWRpb19tdXNpYyIsInJlc3VsdF90b2tlbiI6ImNiZWZmMzM3ZDFmZTk5ZjkyNjlhNjQ2ZjcwZDEyMTFkIiwiYm90X3Rva2VuIjoiMTAzNDcyODI4MCJ9","url":"http://other.web.rh01.sycdn.kuwo.cn/98caf6bd4b1085483565aee8f1ec9f4d/5a5a301b/resource/n3/19/99/1188105613.mp3"}
             */

            private String audioItemId;
            private StreamBean stream;

            public String getAudioItemId() {
                return audioItemId;
            }

            public void setAudioItemId(String audioItemId) {
                this.audioItemId = audioItemId;
            }

            public StreamBean getStream() {
                return stream;
            }

            public void setStream(StreamBean stream) {
                this.stream = stream;
            }

            public static class StreamBean {
                /**
                 * expiryTime : 2018-01-14T00:43:15+08:00
                 * offsetInMilliseconds : 0
                 * progressReport : {"progressReportIntervalInMilliseconds":15000}
                 * streamFormat : AUDIO_MPEG
                 * token : eyJib3RfaWQiOiJhdWRpb19tdXNpYyIsInJlc3VsdF90b2tlbiI6ImNiZWZmMzM3ZDFmZTk5ZjkyNjlhNjQ2ZjcwZDEyMTFkIiwiYm90X3Rva2VuIjoiMTAzNDcyODI4MCJ9
                 * url : http://other.web.rh01.sycdn.kuwo.cn/98caf6bd4b1085483565aee8f1ec9f4d/5a5a301b/resource/n3/19/99/1188105613.mp3
                 */

                private String expiryTime;
                private int offsetInMilliseconds;
                private ProgressReportBean progressReport;
                private String streamFormat;
                private String token;
                private String url;

                public String getExpiryTime() {
                    return expiryTime;
                }

                public void setExpiryTime(String expiryTime) {
                    this.expiryTime = expiryTime;
                }

                public int getOffsetInMilliseconds() {
                    return offsetInMilliseconds;
                }

                public void setOffsetInMilliseconds(int offsetInMilliseconds) {
                    this.offsetInMilliseconds = offsetInMilliseconds;
                }

                public ProgressReportBean getProgressReport() {
                    return progressReport;
                }

                public void setProgressReport(ProgressReportBean progressReport) {
                    this.progressReport = progressReport;
                }

                public String getStreamFormat() {
                    return streamFormat;
                }

                public void setStreamFormat(String streamFormat) {
                    this.streamFormat = streamFormat;
                }

                public String getToken() {
                    return token;
                }

                public void setToken(String token) {
                    this.token = token;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public static class ProgressReportBean {
                    /**
                     * progressReportIntervalInMilliseconds : 15000
                     */

                    private int progressReportIntervalInMilliseconds;

                    public int getProgressReportIntervalInMilliseconds() {
                        return progressReportIntervalInMilliseconds;
                    }

                    public void setProgressReportIntervalInMilliseconds(int progressReportIntervalInMilliseconds) {
                        this.progressReportIntervalInMilliseconds = progressReportIntervalInMilliseconds;
                    }
                }
            }
        }
    }
}
