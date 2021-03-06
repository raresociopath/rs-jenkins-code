package com.raresociopath.jenkins.models

import com.raresociopath.jenkins.util.Cloning

class Repository {
    String humanName
    String safeHumanName
    String shortRepoParam
    String namespace
    String id
    String name
    String defaultBranch
    String language
    String distJobId

    Repository(Map<String, Object> data) {
        this.id = data.id
        this.humanName = data.humanName ?: data.id.capitalize()
        this.name = data.repoName ?: data.id
        this.defaultBranch = data.defaultBranch ?: 'master'
        this.language = data.language
        this.namespace = data.namespace
        this.safeHumanName = humanName.replaceAll('[ -]', "_")
        this.shortRepoParam = "${this.safeHumanName}_Version"
        this.distJobId = data.distJobId
    }

    boolean isJava() {
        language == "java"
    }

    boolean isNode() {
        language == "node"
    }

    boolean isSimpleProject() {
        language == "simple"
    }

    String cloneUrl(ssh, server) {
        if (ssh) "git@$server:${namespace}/${name}.git".toString()
        else "https://$server/${namespace}/${name}.git".toString()
    }

    String cloneUrl(Cloning cloner) {
        cloner.cloneUrl(this)
    }

    def getBuiltVersionFrom(Map data) {
        getFrom(data).builtVersion
    }

    def getVersionFromEnv(env) {
        env.getEnvironment().get(shortRepoParam)
    }

    def getFrom(Map<String, Object> data) {
        def result = data.get(id)
        if (result == null) {
            throw new RuntimeException("input map doesn't contain key $id")
        }
        result
    }
}