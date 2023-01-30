package fr.spoonlabs.flacoco.localization.spectrum.formulas;

public class TarantulaFormula implements Formula{

    public TarantulaFormula(){
    }

    @Override
    public double compute(int nPassingNotExecuting, int nFailingNotExecuting, int nPassingExecuting, int nFailingExecuting) {
        final int nTotalFailing = nFailingExecuting + nFailingNotExecuting;
        final int nTotalPassing = nPassingExecuting + nPassingNotExecuting;
        if ((nTotalFailing == 0) || (nTotalPassing == 0) ||
                ((((double)nFailingExecuting / (double)nTotalFailing) + ((double)nPassingExecuting / (double)nTotalPassing)) == 0.0)){
            return 0;
        }

        return ((double)nFailingExecuting / (double)(nFailingExecuting + nFailingNotExecuting)) /
                (((double)nFailingExecuting / (double)(nFailingExecuting + nFailingNotExecuting)) + ((double)nPassingExecuting /
                        (double)(nPassingExecuting + nPassingNotExecuting)));
    }
}
