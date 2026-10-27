
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

        stage('Deploy') {
            steps {
                sh '''
                    set -eu

                    export IMAGE_TAG="${BUILD_NUMBER}"

                    echo "Deploying image: ${DOCKER_IMAGE}:${IMAGE_TAG}"

                    docker compose config
                    docker compose pull calculator
                    docker compose up -d --no-build

                    echo "Waiting for calculator health..."

                    for i in $(seq 1 30); do
                        if curl --fail --silent \
                            http://localhost:8081/actuator/health \
                            | grep -q '"status":"UP"'; then
                            echo "Calculator is healthy."
                            exit 0
                        fi

                        sleep 2
                    done

                    echo "Health check failed."
                    docker compose ps
                    docker compose logs --tail=100 calculator nginx
                    exit 1
                '''
            }
        }
    }

    post {
        always {
            emailext(
                to: 'harshsinhaed@gmail.com',
                subject: "${currentBuild.currentResult}: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """\
Build status: ${currentBuild.currentResult}

Job: ${env.JOB_NAME}
Build number: ${env.BUILD_NUMBER}

Console output: ${env.BUILD_URL}console
""",
                mimeType: 'text/plain'
            )
        }
    }
}
