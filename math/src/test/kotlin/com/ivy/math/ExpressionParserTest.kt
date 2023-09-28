package com.ivy.math

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ivy.parser.Parser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class ExpressionParserTest {
    private lateinit var parser: Parser<TreeNode>

    @BeforeEach
    fun setUp() {
        parser = expressionParser()
    }

    @ParameterizedTest
    @CsvSource(
        "3+(8/2)*10, 43",
        "2-9*-2+(2+1), 23.0",
        "3+6/3-(-10), 15.0",
        "10-2, 8.0",
        "(20/2)*2, 20.0"
    )
    fun `Test evaluation expression`(expression: String, expected: Double) {
        val result = parser(expression).first()
        val actual = result.value.eval()
        assertThat(actual).isEqualTo(expected)
    }
}