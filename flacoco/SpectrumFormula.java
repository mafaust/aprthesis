package fr.spoonlabs.flacoco.localization.spectrum;

import fr.spoonlabs.flacoco.localization.spectrum.formulas.*;

public enum SpectrumFormula {

	OCHIAI(new OchiaiFormula()),
	BARINEL(new BarinelFormula()),
	DSTAR(new DStarFormula()),
	JACCARD(new JaccardFormula()),
	OP2(new Op2Formula()),
	TARANTULA(new TarantulaFormula());

	private final Formula formula;

	private SpectrumFormula(Formula formula) {
		this.formula = formula;
	}

	public Formula getFormula() {
		return this.formula;
	}

}
