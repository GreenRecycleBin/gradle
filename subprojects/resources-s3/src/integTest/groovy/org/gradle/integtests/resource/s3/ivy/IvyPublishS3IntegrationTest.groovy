/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.integtests.resource.s3.ivy

import org.gradle.api.publish.ivy.AbstractIvyPublishIntegTest
import org.gradle.integtests.fixtures.ToBeFixedForInstantExecution
import org.gradle.integtests.resource.s3.fixtures.S3IntegrationTestPrecondition
import org.gradle.integtests.resource.s3.fixtures.S3Server
import org.junit.Rule
import spock.lang.Requires

@Requires({ S3IntegrationTestPrecondition.fulfilled })
class IvyPublishS3IntegrationTest extends AbstractIvyPublishIntegTest {
    @Rule
    public S3Server server = new S3Server(temporaryFolder)

    def setup() {
        executer.withArgument("-Dorg.gradle.s3.endpoint=${server.getUri()}")
    }

    @ToBeFixedForInstantExecution(ToBeFixedForInstantExecution.Skip.FAILS_TO_CLEANUP)
    def "can publish to an S3 Ivy repository"() {
        given:
        def ivyRepo = server.remoteIvyRepo

        settingsFile << 'rootProject.name = "publishS3Test"'
        buildFile << """
apply plugin: 'java'
apply plugin: 'ivy-publish'

group = 'org.gradle.test'
version = '1.0'

publishing {
    repositories {
        ivy {
            url "${ivyRepo.uri}"
            credentials(AwsCredentials) {
                accessKey "someKey"
                secretKey "someSecret"
            }
        }
    }
    publications {
        ivy(IvyPublication) {
            from components.java
        }
    }
}
"""

        when:
        def module = ivyRepo.module('org.gradle.test', 'publishS3Test', '1.0')
        module.jar.expectUpload()
        module.jar.sha1.expectUpload()
        module.jar.sha256.expectUpload()
        module.jar.sha512.expectUpload()
        module.ivy.expectUpload()
        module.ivy.sha1.expectUpload()
        module.ivy.sha256.expectUpload()
        module.ivy.sha512.expectUpload()
        module.moduleMetadata.expectUpload()
        module.moduleMetadata.sha1.expectUpload()
        module.moduleMetadata.sha256.expectUpload()
        module.moduleMetadata.sha512.expectUpload()

        succeeds 'publish'

        then:
        javaLibrary(module.backingModule).assertPublishedAsJavaModule()
    }
}
