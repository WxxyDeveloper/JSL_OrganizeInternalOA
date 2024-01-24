pipeline {
   environment {
      QODANA_TOKEN=credentials('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJvcmdhbml6YXRpb24iOiJwNTZYRyIsInByb2plY3QiOiIzd1JQNyIsInRva2VuIjoiQWFxVkoifQ.R3TH0E2LgVpBorJ1wyIx3zjIM1p4j-taEEShZKhH2l8')
   }
   agent {
      docker {
         args '''
         -v "${WORKSPACE}":/data/project
         --entrypoint=""
         '''
         image 'jetbrains/qodana-<linter>'
      }
   }
   stages {
      stage('Qodana') {
         steps {
            sh '''qodana'''
         }
      }
   }
}