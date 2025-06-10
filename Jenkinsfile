pipeline {
    agent any

    environment {
        DOCKER_REGISTRY = credentials('DOCKER_REGISTRY')
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Build & Unit Tests') {
            steps {
                script {
                    def services = [
                        'accounts-service', 'api-gateway', 'blocker-service', 'cash-service',
                        'exchange-generator-service', 'exchange-service', 'frontend-service',
                        'notification-service', 'transfer-service'
                    ]

                    def parallelTests = services.collectEntries { service ->
                        ["${service}": {
                            dir(service) {
                                sh 'mvn clean test'
                            }
                        }]
                    }

                    parallel parallelTests
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    def services = [
                        'accounts-service', 'api-gateway', 'blocker-service', 'cash-service',
                        'exchange-generator-service', 'exchange-service', 'frontend-service',
                        'notification-service', 'transfer-service'
                    ]
                    services.each { service ->
                        sh "docker build -t ${DOCKER_REGISTRY}/${service}:${IMAGE_TAG} ${service}"
                    }
                }
            }
        }

        stage('Helm Deploy to TEST') {
            steps {
                script {
                    helmDeploy('test')
                }
            }
        }

        stage('Manual Approval for PROD') {
            steps {
                input message: 'Deploy to PROD environment?', ok: 'Yes, deploy'
            }
        }

        stage('Delete Test helm deploy') {
            steps {
                script {
                    def services = ['kafka'] + [
                        'accounts-service', 'api-gateway', 'blocker-service', 'cash-service',
                        'exchange-generator-service', 'exchange-service', 'frontend-service',
                        'notification-service', 'transfer-service'
                    ]
                    services.each { svc ->
                        sh "helm uninstall ${svc} -n test || true"
                    }
                }
            }
        }

        stage('Helm Deploy to PROD') {
            steps {
                script {
                    helmDeploy('prod')
                }
            }
        }
    }
}

def helmDeploy(namespace) {
    def services = [
        [name: 'accounts-service', host: 'customer'],
        [name: 'api-gateway', host: 'order'],
        [name: 'blocker-service', host: 'customer'],
        [name: 'cash-service', host: 'customer'],
        [name: 'exchange-generator-service', host: 'customer'],
        [name: 'exchange-service', host: 'customer'],
        [name: 'frontend-service', host: 'customer'],
        [name: 'notification-service', host: 'customer'],
        [name: 'transfer-service', host: 'customer']
    ]

    sh "helm dependency build helm/charts/kafka"

    sh """
        helm upgrade --install kafka helm/charts/kafka \\
        --namespace ${namespace} --create-namespace \\
        -f helm/charts/kafka/values.yaml
    """

    services.each { svc ->
        def chart = "helm/charts/${svc.name}"
        def host = "${svc.host}.${namespace}.local"
        sh """
            helm upgrade --install ${svc.name} ${chart} \\
            --namespace ${namespace} --create-namespace \\
            --set image.repository=${DOCKER_REGISTRY}/${svc.name} \\
            --set image.tag=${IMAGE_TAG} \\
            --set ingress.enabled=true \\
            --set ingress.hosts[0].host=${host} \\
            --set ingress.hosts[0].paths[0].path="/" \\
            --set ingress.hosts[0].paths[0].pathType="ImplementationSpecific"
        """
    }
}
