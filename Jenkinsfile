
pipeline {
    agent any

    environment {
        JAVA_HOME = '/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home'
        PATH = "${JAVA_HOME}/bin:/opt/homebrew/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"
        DOCKER_IMAGE = 'iharshsinha/calculator-api'
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/TEST-*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    docker build \
                      -t ${DOCKER_IMAGE}:${BUILD_NUMBER} \
                      -t ${DOCKER_IMAGE}:latest .
                '''
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'calculator-dockerhub',
                    usernameVariable: 'DOCKERHUB_USER',
                    passwordVariable: 'DOCKERHUB_TOKEN'
                )]) {
                    sh '''
                        set +x
                        echo "$DOCKERHUB_TOKEN" | docker login \
                          --username "$DOCKERHUB_USER" \
                          --password-stdin

                        docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}
                        docker push ${DOCKER_IMAGE}:latest

                        docker logout
                    '''
                }
            }
        }
    }
}
