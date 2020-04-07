#!/usr/bin/env bash

function LOG {
    log_level=$1
    message=$2
    echo [`date +"%Y-%m-%d %H:%M:%S"`] [${log_level}] : ${message}
}

function chkVars () {
    var=$1
    if [ -z "${!var}" ]; then
        LOG ERROR "$var not defined, not proceeding.. Please define them in local-env-overrides.sh"
        exit 1
    fi
}

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

LOG INFO "Adding typesafe-activator to PATH"
export PATH=$PATH:$DIR/typesafe-activator/


LOG INFO "Sourcing configuration"

cd $DIR

source local-env-defaults.sh

overrides=local-env-overrides.sh

if [ -e "$overrides" ]; then
  source $overrides
fi

chkVars mysql_root_user

LOG INFO "Setting up db schema. You might be prompted for mysql credentials"
mysql -u"$mysql_root_user" -p < ./dr-elephant-db-schema.sql

LOG INFO "All done"

