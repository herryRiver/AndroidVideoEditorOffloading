/*
 * Copyright (c) 2009-2011, The Regents of the University of California,
 * through Lawrence Berkeley National Laboratory (subject to receipt of any
 * required approvals from the U.S. Dept. of Energy).  All rights reserved.
 *
 * This code is distributed under a BSD style license, see the LICENSE file
 * for complete information.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <getopt.h>
#include <errno.h>
#include <signal.h>
#include <unistd.h>
#include <stdint.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdint.h>
#include <netinet/tcp.h>
#include <stdbool.h>
#include "iperf.h"
#include "iperf_api.h"
#include "iperf_client_api.h"
//#include "iperf_server_api.h"
#include "units.h"
#include "locale.h"
#include "iperf_error.h"
#include "net.h"
#include "jni.h"
int iperf_run(struct iperf_test *);
struct iperf_test* main_launch(int argc, char **argv);
struct iperf_test *gTest = NULL;
/**************************************************************************/

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
  JNIEnv* env;
  if ((*vm)->GetEnv(vm, (void**)(&env), JNI_VERSION_1_6) != JNI_OK) {
    return -1;
  }

  //  Get jclass with env->FindClass.
  // Register methods with env->RegisterNatives.

  return JNI_VERSION_1_6;
}

JNIEXPORT jboolean JNICALL
Java_com_cs4911_video_1editor_android_BandwidthMeasurement_launchBandwidthTest(JNIEnv *jenv, jobject  obj, jstring serverIp)
{
    if (gTest)
        iperf_free_test(gTest);

    gTest = NULL;
    jboolean is_copy;
    const jbyte *server_ip_j = (*jenv)->GetStringUTFChars(jenv, serverIp, &is_copy) ;

    char *server_ip = malloc(strlen(server_ip_j) + 1);
    strncpy(server_ip, server_ip_j, strlen(server_ip_j));
    server_ip[strlen(server_ip_j)] = '\0';
    LOGI("server ip from main: %s", server_ip);
    char *argv[] = {"iperf", "-c", server_ip};
	gTest = main_launch(3, argv);

    if (!gTest)
        return 1;
    return 0;
}

JNIEXPORT jlong JNICALL
Java_com_cs4911_video_1editor_android_BandwidthMeasurement_getUploadedBytes(JNIEnv *jenv, jobject  obj)
{
	if (!gTest)
		return -1;

    LOGI("total sent : %llu\n", gTest->total_sent);
	return gTest->total_sent;
}

JNIEXPORT jlong JNICALL
Java_com_cs4911_video_1editor_android_BandwidthMeasurement_getHostCpuUtilization(JNIEnv *jenv, jobject  obj)
{
	if (!gTest)
		return -1;

	return gTest->cpu_util;
}

JNIEXPORT jlong JNICALL
Java_com_cs4911_video_1editor_android_BandwidthMeasurement_getServerCpuUtilization(JNIEnv *jenv, jobject  obj)
{
	if (!gTest)
		return -1;

	return gTest->remote_cpu_util;
}

JNIEXPORT jlong JNICALL
Java_com_cs4911_video_1editor_android_BandwidthMeasurement_getDownloadedBytes(JNIEnv *jenv, jobject  obj)
{
	if (!gTest)
		return -1;

    LOGI("total recv : %llu\n", gTest->total_recv);
	return gTest->total_recv;
}

JNIEXPORT jdouble JNICALL
Java_com_cs4911_video_1editor_android_BandwidthMeasurement_getTimeTaken(JNIEnv *jenv, jobject  obj)
{
	if (!gTest)
		return -1;

	return gTest->total_time;
}

JNIEXPORT jlong JNICALL
Java_com_cs4911_video_1editor_android_BandwidthMeasurement_launchLatencyTest(JNIEnv * env, jobject  obj)
{
    return start_ping();   
}

struct iperf_test*
main_launch(int argc, char **argv)
{
    struct iperf_test *test;

    // XXX: Setting the process affinity requires root on most systems.
    //      Is this a feature we really need?
#ifdef TEST_PROC_AFFINITY
    /* didnt seem to work.... */
    /*
     * increasing the priority of the process to minimise packet generation
     * delay
     */
    int rc = setpriority(PRIO_PROCESS, 0, -15);

    if (rc < 0) {
        perror("setpriority:");
        printf("setting priority to valid level\n");
        rc = setpriority(PRIO_PROCESS, 0, 0);
    }
    
    /* setting the affinity of the process  */
    cpu_set_t cpu_set;
    int affinity = -1;
    int ncores = 1;

    sched_getaffinity(0, sizeof(cpu_set_t), &cpu_set);
    if (errno)
        perror("couldn't get affinity:");

    if ((ncores = sysconf(_SC_NPROCESSORS_CONF)) <= 0)
        err("sysconf: couldn't get _SC_NPROCESSORS_CONF");

    CPU_ZERO(&cpu_set);
    CPU_SET(affinity, &cpu_set);
    if (sched_setaffinity(0, sizeof(cpu_set_t), &cpu_set) != 0)
        err("couldn't change CPU affinity");
#endif

    test = iperf_new_test();
    if (!test) {
        iperf_error("create new test error");
        return NULL;
    }
    iperf_defaults(test);	/* sets defaults */

    // XXX: Check signal for errors?
    signal(SIGINT, sig_handler);
    if (setjmp(env)) {
        if (test->ctrl_sck >= 0) {
            test->state = (test->role == 'c') ? CLIENT_TERMINATE : SERVER_TERMINATE;
            if (Nwrite(test->ctrl_sck, &test->state, sizeof(char), Ptcp) < 0) {
                i_errno = IESENDMESSAGE;
                return NULL;
            }
        }
        return NULL;
    } 

    LOGI("signal pass");
    if (iperf_parse_arguments(test, argc, argv) < 0) {
        LOGE("parameter error");
        iperf_error("parameter error");
        fprintf(stderr, "\n");
        usage_long();
        return NULL;
    }

    LOGI("params pass");

    if (iperf_run(test) < 0) {
        iperf_error("error");
        return NULL;
    }

    LOGI("\niperf Done.\n");

    return test;
}

/**************************************************************************/
int
iperf_run(struct iperf_test * test)
{
    switch (test->role) {
        case 'c':
            if (iperf_run_client(test) < 0) {
                iperf_error("error");
                return (-1);
            }
            break;
        default:
            usage();
            break;
    }

    return (0);
}

