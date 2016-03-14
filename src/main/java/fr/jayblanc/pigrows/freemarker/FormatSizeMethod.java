package fr.jayblanc.pigrows.freemarker;

import java.util.List;

import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class FormatSizeMethod implements TemplateMethodModelEx {

	private String parse(long bytes) {
		if (bytes < 1000) {
			return bytes + " B";
		}
		int exp = (int) (Math.log(bytes) / Math.log(1000));
		String pre = "kMGTPE".charAt(exp - 1) + "";
		return String.format("%.1f %sB", bytes / Math.pow(1000, exp), pre);
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		if (arguments.size() != 1) {
			throw new TemplateModelException("This method nedd one parameter.");
		}
		return parse(((SimpleNumber)arguments.get(0)).getAsNumber().longValue());
	}

}
