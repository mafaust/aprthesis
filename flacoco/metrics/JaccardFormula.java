package fr.spoonlabs.flacoco.localization.spectrum.formulas;

public class JaccardFormula implements Formula {
    public JaccardFormula() {
    }

    @Override
    public double compute(int nPassingNotExecuting, int nFailingNotExecuting, int nPassingExecuting, int nFailingExecuting) {

        if ((nFailingExecuting + nFailingNotExecuting + nPassingExecuting) == 0) {
            return 0;
        }
        return (double)nFailingExecuting / (double)(nFailingExecuting + nFailingNotExecuting + nPassingExecuting);
    }
}
