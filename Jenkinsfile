pipeline {
    agent any

    environment {
        DOCKER_REGISTRY = credentials('DOCKER_REGISTRY')
        DB_PASSWORD     = credentials('DB_PASSWORD')
        GITHUB_USERNAME = credentials('GITHUB_USERNAME')
        DB_NAME         = 'mydb'
        DB_USER         = 'myuser'
        IMAGE_TAG       = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Build & Unit Tests') {
            parallel {
                stage('Accounts Service') {
                    steps {
                        dir('accounts-service') {
                            sh 'mvn clean test'
                        }
                    }
                }
                stage('Api Gateway') {
                    steps {
                        dir('api-gateway') {
                            sh 'mvn clean test'
                        }
                    }
                }
                stage('Blocker Service') {
                    steps {
                        dir('blocker-service') {
                            sh 'mvn clean test'
                        }
                    }
                }
                stage('Cash Service') {
                    steps {
                        dir('cash-service') {
                            sh 'mvn clean test'
                        }
                    }
                }
                stage('Exchange Generator Service') {
                    steps {
                        dir('exchange-generator-service') {
                            sh 'mvn clean test'
                        }
                    }
                }
                stage('Exchange Service') {
                    steps {
                        dir('exchange-service') {
                            sh 'mvn clean test'
                        }
                    }
                }
                stage('Frontend Service') {
                    steps {
                        dir('frontend-service') {
                            sh 'mvn clean test'
                        }
                    }
                }
                stage('Notification Service') {
                    steps {
                        dir('notification-service') {
                            sh 'mvn clean test'
                        }
                    }
                }
                stage('Transfer Service') {
                    steps {
                        dir('transfer-service') {
                            sh 'mvn clean test'
                        }
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh """
                docker build -t ${DOCKER_REGISTRY}/accounts-service:${IMAGE_TAG} accounts-service
                docker build -t ${DOCKER_REGISTRY}/api-gateway:${IMAGE_TAG} api-gateway
                docker build -t ${DOCKER_REGISTRY}/blocker-service:${IMAGE_TAG} blocker-service
                docker build -t ${DOCKER_REGISTRY}/cash-service:${IMAGE_TAG} cash-service
                docker build -t ${DOCKER_REGISTRY}/exchange-generator-service:${IMAGE_TAG} exchange-generator-service
                docker build -t ${DOCKER_REGISTRY}/exchange-service:${IMAGE_TAG} exchange-service
                docker build -t ${DOCKER_REGISTRY}/frontend-service:${IMAGE_TAG} frontend-service
                docker build -t ${DOCKER_REGISTRY}/notification-service:${IMAGE_TAG} notification-service
                docker build -t ${DOCKER_REGISTRY}/transfer-service:${IMAGE_TAG} transfer-service
                """
            }
        }

        stage('Helm Deploy to TEST') {
            steps {
                sh """
                helm upgrade --install accounts-service helm/charts/accounts-service \\
                  --namespace test --create-namespace \\
                  --set image.repository=${DOCKER_REGISTRY}/accounts-service \\
                  --set image.tag=${IMAGE_TAG} \\
                  --set ingress.enabled=true \\
                  --set ingress.hosts[0].host=customer.test.local \\
                  --set ingress.hosts[0].paths[0].path="/" \\
                  --set ingress.hosts[0].paths[0].pathType="ImplementationSpecific"

                helm upgrade --install api-gateway helm/charts/api-gateway \\
                  --namespace test --create-namespace \\
                  --set image.repository=${DOCKER_REGISTRY}/api-gateway \\
                  --set image.tag=${IMAGE_TAG} \\
                  --set ingress.enabled=true \\
                  --set ingress.hosts[0].host=order.test.local \\
                  --set ingress.hosts[0].paths[0].path="/" \\
                  --set ingress.hosts[0].paths[0].pathType="ImplementationSpecific"

                helm upgrade --install blocker-service helm/charts/blocker-service \\
                  --namespace test --create-namespace \\
                  --set image.repository=${DOCKER_REGISTRY}/blocker-service \\
                  --set image.tag=${IMAGE_TAG} \\
                  --set ingress.enabled=true \\
                  --set ingress.hosts[0].host=customer.test.local \\
                  --set ingress.hosts[0].paths[0].path="/" \\
                  --set ingress.hosts[0].paths[0].pathType="ImplementationSpecific"

                helm upgrade --install cash-service helm/charts/cash-service \\
                  --namespace test --create-namespace \\
                  --set image.repository=${DOCKER_REGISTRY}/cash-service \\
                  --set image.tag=${IMAGE_TAG} \\
                  --set ingress.enabled=true \\
                  --set ingress.hosts[0].host=customer.test.local \\
                  --set ingress.hosts[0].paths[0].path="/" \\
                  --set ingress.hosts[0].paths[0].pathType="ImplementationSpecific"

                helm upgrade --install exchange-generator-service helm/charts/exchange-generator-service \\
                  --namespace test --create-namespace \\
                  --set image.repository=${DOCKER_REGISTRY}/exchange-generator-service \\
                  --set image.tag=${IMAGE_TAG} \\
                  --set ingress.enabled=true \\
                  --set ingress.hosts[0].host=customer.test.local \\
                  --set ingress.hosts[0].paths[0].path="/" \\
                  --set ingress.hosts[0].paths[0].pathType="ImplementationSpecific"

                helm upgrade --install exchange-service helm/charts/exchange-service \\
                  --namespace test --create-namespace \\
                  --set image.repository=${DOCKER_REGISTRY}/exchange-service \\
                  --set image.tag=${IMAGE_TAG} \\
                  --set ingress.enabled=true \\
                  --set ingress.hosts[0].host=customer.test.local \\
                  --set ingress.hosts[0].paths[0].path="/" \\
                  --set ingress.hosts[0].paths[0].pathType="ImplementationSpecific"

                helm upgrade --install frontend-service helm/charts/frontend-service \\
                  --namespace test --create-namespace \\
                  --set image.repository=${DOCKER_REGISTRY}/frontend-service \\
                  --set image.tag=${IMAGE_TAG} \\
                  --set ingress.enabled=true \\
                  --set ingress.hosts[0].host=customer.test.local \\
                  --set ingress.hosts[0].paths[0].path="/" \\
                  --set ingress.hosts[0].paths[0].pathType="ImplementationSpecific"

                helm upgrade --install notification-service helm/charts/notification-service \\
                  --namespace test --create-namespace \\
                  --set image.repository=${DOCKER_REGISTRY}/notification-service \\
                  --set image.tag=${IMAGE_TAG} \\
                  --set ingress.enabled=true \\
                  --set ingress.hosts[0].host=customer.test.local \\
                  --set ingress.hosts[0].paths[0].path="/" \\
                  --set ingress.hosts[0].paths[0].pathType="ImplementationSpecific"

                helm upgrade --install transfer-service helm/charts/transfer-service \\
                  --namespace test --create-namespace \\
                  --set image.repository=${DOCKER_REGISTRY}/transfer-service \\
                  --set image.tag=${IMAGE_TAG} \\
                  --set ingress.enabled=true \\
                  --set ingress.hosts[0].host=customer.test.local \\
                  --set ingress.hosts[0].paths[0].path="/" \\
                  --set ingress.hosts[0].paths[0].pathType="ImplementationSpecific"
                """
            }
        }

        stage('Manual Approval for PROD') {
            steps {
                input message: 'Deploy to PROD environment?', ok: 'Yes, deploy'
            }
        }

        stage('Install PostgreSQL to PROD') {
            steps {
                sh """
                helm upgrade --install postgres bitnami/postgresql \\
                  --namespace prod --create-namespace \\
                  --set auth.database=${DB_NAME} \\
                  --set auth.username=${DB_USER} \\
                  --set auth.password=${DB_PASSWORD}
                """
            }
        }

        stage('Create DB Secrets for PROD') {
            steps {
                sh """
                kubectl create secret generic customer-service-customer-db \\
                  --from-literal=password=${DB_PASSWORD} \\
                  -n prod --dry-run=client -o yaml | kubectl apply -f -

                kubectl create secret generic order-service-order-db \\
                  --from-literal=password=${DB_PASSWORD} \\
                  -n prod --dry-run=client -o yaml | kubectl apply -f -
                """
            }
        }

        stage('Helm Deploy to PROD') {
            steps {
                sh """
                helm upgrade --install customer-service my-microservices-app/charts/customer-service \\
                  --namespace prod --create-namespace \\
                  --set image.repository=${DOCKER_REGISTRY}/customer-service \\
                  --set image.tag=${IMAGE_TAG} \\
                  --set ingress.enabled=true \\
                  --set ingress.hosts[0].host=customer.prod.local \\
                  --set ingress.hosts[0].paths[0].path="/" \\
                  --set ingress.hosts[0].paths[0].pathType="ImplementationSpecific"

                helm upgrade --install order-service my-microservices-app/charts/order-service \\
                  --namespace prod --create-namespace \\
                  --set image.repository=${DOCKER_REGISTRY}/order-service \\
                  --set image.tag=${IMAGE_TAG} \\
                  --set ingress.enabled=true \\
                  --set ingress.hosts[0].host=order.prod.local \\
                  --set ingress.hosts[0].paths[0].path="/" \\
                  --set ingress.hosts[0].paths[0].pathType="ImplementationSpecific"
                """
            }
        }
    }
}