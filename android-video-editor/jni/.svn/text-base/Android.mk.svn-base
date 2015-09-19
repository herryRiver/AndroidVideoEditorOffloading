LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := launchbed
LOCAL_SRC_FILES := main.c iperf_api.c iperf_client_api.c iperf_error.c iperf_tcp.c timer.c locale.c net.c units.c tcp_info.c iperf_udp.c iperf_util.c ping.c
LOCAL_LDLIBS	:= -llog

include $(BUILD_SHARED_LIBRARY)
