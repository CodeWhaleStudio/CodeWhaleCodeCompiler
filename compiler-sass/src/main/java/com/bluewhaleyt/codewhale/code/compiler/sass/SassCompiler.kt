package com.bluewhaleyt.codewhale.code.compiler.sass

import android.content.Context
import android.util.Log
import com.bluewhaleyt.codewhale.code.compiler.core.CompileReporter
import com.bluewhaleyt.codewhale.code.compiler.core.Compiler
import de.larsgrefer.sass.embedded.SassCompiler
import de.larsgrefer.sass.embedded.android.AndroidSassCompilerFactory

class SassCompiler(
    private val context: Context,
    override val reporter: CompileReporter,
    val options: SassCompilerOptions,
) : Compiler<SassCompilerOptions>(reporter, options) {

    private val compiler = AndroidSassCompilerFactory.bundled(context)

    override fun compile(): SassCompilationResult {
        reporter.reportInfo("Running compiler powered by ${SassCompiler::class.java.name}...")
        val compilationResult = SassCompilationResult()
        try {
            if (options.file.isFile) {
                when (options.file.extension.lowercase()) {
                    "css", "scss", "sass" -> reporter.reportInfo("Compiling ${options.file.name}...")
                    else -> reporter.reportError("Invalid file type, file must be in either css, sass or scss format.")
                }
                val compilationSuccess = compiler.compileFile(options.file)
                compilationResult.apply {
                    output = null
                    css = compilationSuccess.css
                }
                reporter.reportSuccess("Compiled successfully, no output is printed.")
            }
        } catch (e: Throwable) {
            compilationResult.error = e
            reporter.reportError("Compilation failed.")
        }
        return compilationResult
    }

}