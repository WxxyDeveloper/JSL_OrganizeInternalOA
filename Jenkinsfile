pipeline {
    environment {
        QODANA_TOKEN = credentials('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJvcmdhbml6YXRpb24iOiJwNTZYRyIsInByb2plY3QiOiIzd1JQNyIsInRva2VuIjoiMzdiYmIifQ.neuOR5TzO3GpuexxQT4SM-5N4BxF5aRxujtyRpDpTB0')
    }
    agent {
        docker {
            args '''
                -v "${WORKSPACE}":/data/project
                --entrypoint=""
                '''
            image 'jetbrains/qodana-jvm'
        }
    }
    stages {
        stage('Qodana') {
            when {
                branch 'feature'
            }
            steps {
                sh '''qodana'''
            }
        }
    }
}