#! /bin/bash
# Use for SkyExchange deployment
# 2019/02/19 Jason      UAT
# 2019/02/28 Jason      UAT add .log .err with early termination


# font style
BLACK=`tput setaf 0`
RED=`tput setaf 1`
GREEN=`tput setaf 2`
YELLOW=`tput setaf 3`
BLUE=`tput setaf 4`
MAGENTA=`tput setaf 5`
CYAN=`tput setaf 6`
WHITE=`tput setaf 7`
BOLD=`tput bold`
RESET=`tput sgr0`

# file var
export JOBNAME=`basename $0 .sh`
export TIMESTAMP=`date +%Y%m%d%H%M%S`
export LOG_DIR=/home/sysop/logs
export JOBLOG=${LOG_DIR}/${JOBNAME}_${TIMESTAMP}.log
export JOBERR=${LOG_DIR}/${JOBNAME}_${TIMESTAMP}.err

# deploy var
export SYSOP_HOME_DIR=/home/sysop
export APP_NAME=servicedesk
export APP_FILENAME=${APP_NAME}.war

# when error is found, terminate the program
f_checkpoint(){
    if [[ -s ${JOBERR} ]]; then
        echo "${RED}${BOLD}ERROR. Deploy terminated.${RESET}" >> ${JOBLOG} >> ${JOBERR}
        echo "log file: "${JOBLOG}
        echo "err file: "${JOBERR}
        exit
    fi
}

# echo on screen and on log file
echo_log(){
    echo "${GREEN}${BOLD}"$1"${RESET}"
    echo "[echo_log]"$1 >> ${JOBLOG}
}


# Stop Tomcat Server
echo ""
echo_log "Stopping Tomcat Server..."
/etc/init.d/tomcat stop 1>>${JOBLOG} 2>>sed  '/^NOTE: /d' ${JOBERR}
f_checkpoint

# Copy to-be-deployed application
echo ""
echo_log "Copying to-be-deployed ${APP_FILENAME}..."
cp "${SYSOP_HOME_DIR}/${APP_FILENAME}" "${SYSOP_HOME_DIR}/${APP_FILENAME}.${TIMESTAMP}" 1>>${JOBLOG} 2>>${JOBERR}
cp "${SYSOP_HOME_DIR}/${APP_FILENAME}" "${CATALINA_HOME}/webapps/${APP_FILENAME}" 1>>${JOBLOG} 2>>${JOBERR}
chown tomcat:tomcat "${CATALINA_HOME}/webapps/${APP_FILENAME}" 1>>${JOBLOG} 2>>${JOBERR}
rm -f "${SYSOP_HOME_DIR}/${APP_FILENAME}" 1>>${JOBLOG} 2>>${JOBERR}
f_checkpoint

# Ensure permission for tomcat work dir
echo ""
echo_log "Updating permission on tomcat work dir..."
chown tomcat:tomcat -R "${CATALINA_HOME}/work/" 1>>${JOBLOG} 2>>${JOBERR}
f_checkpoint

# Start Tomcat Server
echo ""
echo_log "Starting Tomcat Server (with deployment)..."
/etc/init.d/tomcat start 1>>${JOBLOG} 2>>${JOBERR}
f_checkpoint


# print deploy & app log file
echo_log `basename $0`" has deployed with no error." >> ${JOBLOG}
echo "deploy log: "${JOBLOG}
echo "${BOLD}tail -f ${CATALINA_HOME}/logs/${APP_NAME}.log${RESET}"
exit
