package fr.spoonlabs.flacoco.localization.spectrum.formulas;

public class Op2Formula implements Formula{

    public Op2Formula(){
    }

    @Override
    public double compute(int nPassingNotExecuting, int nFailingNotExecuting, int nPassingExecuting, int nFailingExecuting) {
        return (double)nFailingExecuting - ((double)nPassingExecuting / ((nPassingExecuting + nPassingNotExecuting) + 1.0));
    }
}
