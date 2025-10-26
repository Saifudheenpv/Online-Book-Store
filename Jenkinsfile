pipeline {
    agent any

    environment {
        // SonarQube
        SONARQUBE = 'Sonar-Server'
        SCANNER = 'Sonar-Scanner'

        // Docker
        DOCKER_CREDENTIALS = credentials('dockerhub-creds')
        DOCKER_IMAGE = "saifudheenpv/online-book-store"

        // Jenkins Tools
        MAVEN_HOME = tool 'Maven3'
        JAVA_HOME = tool 'JDK21'
        PATH = "$MAVEN_HOME/bin:$JAVA_HOME/bin:$PATH"

        // Email (Gmail SSL)
        SMTP_CREDENTIALS = credentials('gmail-smtp')
    }

    triggers {
        githubPush()
    }

    stages {

        stage('Checkout') {
            steps {
                echo "🌀 Checkout Stage"
                git branch: 'main', url: 'https://github.com/saifudheenpv/Online-Book-Store.git'
            }
        }

        stage('Maven Compile & Test') {
            steps {
                echo "⚙️ Maven Compile & Test Stage"
                sh 'mvn clean compile test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo "🔍 SonarQube Analysis Stage"
                withSonarQubeEnv('Sonar-Server') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=Online-Book-Store -Dsonar.projectName="Online Book Store"'
                }
            }
        }

        stage('OWASP Dependency Check') {
            steps {
                echo "🛡️ Dependency Check Stage"
                sh 'mvn org.owasp:dependency-check-maven:check -Dformat=HTML'
            }
        }

        stage('Maven Build') {
            steps {
                echo "📦 Maven Build Stage"
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build & Push') {
            steps {
                echo "🐳 Docker Build & Push Stage"
                sh """
                docker build -t $DOCKER_IMAGE:latest .
                echo $DOCKER_CREDENTIALS_PSW | docker login -u $DOCKER_CREDENTIALS_USR --password-stdin
                docker push $DOCKER_IMAGE:latest
                """
            }
        }

        stage('Trivy Scan') {
            steps {
                echo "🔎 Trivy Scan Stage"
                sh 'trivy image $DOCKER_IMAGE:latest || true'
            }
        }

        stage('Deploy Container') {
            steps {
                echo "🚀 Deploy Container Stage"
                sh """
                docker stop onlinebookstore || true
                docker rm onlinebookstore || true
                docker run -d --name onlinebookstore -p 8081:8080 $DOCKER_IMAGE:latest
                """
            }
        }
    }

    post {
        success {
            echo "✅ Build Success! Sending email..."
            emailext (
                subject: "✅ SUCCESS: Jenkins Build #${env.BUILD_NUMBER} - ${env.JOB_NAME}",
                body: """<h3>Build Succeeded!</h3>
                        <p>Job: ${env.JOB_NAME}</p>
                        <p>Build #: ${env.BUILD_NUMBER}</p>
                        <p>Check details: <a href='${env.BUILD_URL}'>${env.BUILD_URL}</a></p>""",
                to: 'mesaifudheenpv@gmail.com',
                replyTo: 'mesaifudheenpv@gmail.com',
                from: SMTP_CREDENTIALS_USR
            )
        }

        failure {
            echo "❌ Build Failed! Sending email..."
            emailext (
                subject: "❌ FAILURE: Jenkins Build #${env.BUILD_NUMBER} - ${env.JOB_NAME}",
                body: """<h3>Build Failed!</h3>
                        <p>Job: ${env.JOB_NAME}</p>
                        <p>Build #: ${env.BUILD_NUMBER}</p>
                        <p>Check details: <a href='${env.BUILD_URL}'>${env.BUILD_URL}</a></p>""",
                to: 'mesaifudheenpv@gmail.com',
                replyTo: 'mesaifudheenpv@gmail.com',
                from: SMTP_CREDENTIALS_USR
            )
        }
    }
}
