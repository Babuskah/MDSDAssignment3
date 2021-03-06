/*
 * generated by Xtext 2.25.0
 */
package dk.sdu.mmmi.mdsd.generator

import dk.sdu.mmmi.mdsd.math.Div
import dk.sdu.mmmi.mdsd.math.LetBinding
import dk.sdu.mmmi.mdsd.math.MathExp
import dk.sdu.mmmi.mdsd.math.MathNumber
import dk.sdu.mmmi.mdsd.math.Minus
import dk.sdu.mmmi.mdsd.math.Mult
import dk.sdu.mmmi.mdsd.math.Plus
import dk.sdu.mmmi.mdsd.math.VarBinding
import dk.sdu.mmmi.mdsd.math.VariableUse
import java.util.HashMap
import java.util.Map
import javax.swing.JOptionPane
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import dk.sdu.mmmi.mdsd.math.External
import dk.sdu.mmmi.mdsd.math.Program
import java.util.ArrayList
import java.util.Arrays
import dk.sdu.mmmi.mdsd.math.ExtCall
import dk.sdu.mmmi.mdsd.math.Parenthesis

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
 
class MathGenerator extends AbstractGenerator {
	
	static Map<String, Integer> variables;

	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
		
		for (math : resource.allContents.toIterable.filter(Program)) {
			fsa.generateFile("math_expression/" + math.name + ".java", math.generateProgram)
		}
		//val math = resource.allContents.filter(Program).next
		//val result = generateProgram(math)
		//fsa.generateFile("math_expression/" + math.name + ".java", math.generateProgram)
		
	}
	
	def generateProgram(Program program) {
		val classname = program.name
		return'''
							package math_expression;
							import java.util.*;
							public class <<classname>> {
							<<FOR variable : program.math.variables>>
							public int <<variable.name>>;
							<<ENDFOR>>
							<<IF program.externals.size != 0>>private External external<<ENDIF>>
							public <<classname>>(<<IF program.externals.size != 0>>External external<<ENDIF>>) {
							<<IF program.externals.size != 0>>this.external = external;<<ENDIF>>
							}
							public void compute() {
							<<FOR variable : program.math.variables>>
							<<variable.name>> = <<variable.computeExpression()>>;
							<<ENDFOR>>
							}
							public interface External{
							<<FOR external : externals>>
							public int <<external.name>>(<<FOR arguments : external.args SEPARATOR ',' >><<arguments>> <<Character.toChars(characterCounter++).toString().substring(1,2)>><<ENDFOR>>);
							<<ENDFOR>>
							}
							}
		'''
	}
		
//	def void displayPanel(Map<String, Integer> result) {
//		var resultString = ""
//		for (entry : result.entrySet()) {
//         	resultString += "var " + entry.getKey() + " = " + entry.getValue() + "\n"
//        }
//		
//		JOptionPane.showMessageDialog(null, resultString ,"Math Language", JOptionPane.INFORMATION_MESSAGE)
//	}
	
	def static compute(MathExp math) {
		variables = new HashMap()
		for(varBinding: math.variables)
			varBinding.computeExpression()
		variables
	}
	
	def static dispatch CharSequence computeExpression(VarBinding binding) {
		System.out.println(binding.name)
		binding.expression.computeExpression
	}
	
	def static dispatch CharSequence computeExpression(MathNumber exp) {
		System.out.println(exp.value)
		'''<<exp.value>>'''
	}

	def static dispatch CharSequence computeExpression(Plus exp) {
		'''<<exp.left.computeExpression>> + <<exp.right.computeExpression>>'''
	}
	
	def static dispatch CharSequence computeExpression(Minus exp) {
		'''<<exp.left.computeExpression>> - <<exp.right.computeExpression>>'''
	}
	
	def static dispatch CharSequence computeExpression(Mult exp) {
		'''<<exp.left.computeExpression>> * <<exp.right.computeExpression>>'''
	}
	
	def static dispatch CharSequence computeExpression(Div exp) {
		'''<<exp.left.computeExpression>> / <<exp.right.computeExpression>>'''
	}

	def static dispatch CharSequence computeExpression(LetBinding exp) {
		exp.body.computeExpression
	}
	
	def static dispatch CharSequence computeExpression(VariableUse exp) {
		exp.ref.computeBinding
	}

	def static dispatch CharSequence computeBinding(VarBinding binding){
		if(!variables.containsKey(binding.name))
			binding.computeExpression()			
		'''<<binding.name>>'''
	}
	
	def static dispatch String computeBinding(LetBinding binding){
		'''(<<binding.binding.computeExpression>>)'''
	}
	
	def static dispatch CharSequence computeExpression(Parenthesis exp) {
		'''(<<exp.exp.computeExpression>>)'''
	}
	
	def static dispatch CharSequence computeExpression(ExtCall exp) {
		return '''this.external.<<exp.ref.name>>(<<FOR expression:exp.args  SEPARATOR ',' >><<expression.computeExpression>><<ENDFOR>>)'''
	}
		
}



