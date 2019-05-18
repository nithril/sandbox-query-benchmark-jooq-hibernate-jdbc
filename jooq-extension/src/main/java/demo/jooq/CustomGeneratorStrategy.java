package demo.jooq;

import org.jooq.tools.StringUtils;
import org.jooq.codegen.DefaultGeneratorStrategy;
import org.jooq.meta.Definition;

/**
 * Created by nlabrot on 28/09/15.
 */
public class CustomGeneratorStrategy extends DefaultGeneratorStrategy {

	@Override
	public String getJavaClassName(Definition definition, Mode mode) {
		StringBuilder result = new StringBuilder();

		if (mode == Mode.RECORD) {
			result.append('R');
		} else if (mode == Mode.DEFAULT) {
			result.append('T');
		} else if (mode == Mode.INTERFACE) {
			result.append('I');
		}

		result.append(StringUtils.toCamelCase(definition.getOutputName()));

		if (mode == Mode.RECORD) {
			result.append("Record");
		} else if (mode == Mode.DAO) {
			result.append("Dao");
		}

		return result.toString();

	}
}
