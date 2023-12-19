package org.example.apifueling.cucumber

import org.junit.platform.suite.api.*
import io.cucumber.junit.platform.engine.Constants

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameters(
    value = [
        ConfigurationParameter(
            key = Constants.GLUE_PROPERTY_NAME,
            value = "org.example.apifueling.cucumber"
        ),
        ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty"),
        ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
    ]
)
class CucumberSuite