package parsing

import VoidError
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.CommonToken
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import parsing.structure.ErrorLogger

class SyntaxError(
    line: Int,
    pos: Int,
    source: String? = null,
    private val offendingSymbol: Any?,
    private val msg: String?
): VoidError(line, pos, source) {
    override fun getErrorMsg(): String = "syntax error" +
            when (offendingSymbol) {
                is CommonToken -> " - unexpected token \"${offendingSymbol.text}\""
                else -> msg
            }
}

class EventErrorListener(private val file: String? = null): BaseErrorListener() {
    val errorLogger = ErrorLogger(file ?: "input")

    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        errorLogger.publish(SyntaxError(line, charPositionInLine, file, offendingSymbol, msg))
    }
}