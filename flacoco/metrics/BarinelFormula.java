package fr.spoonlabs.flacoco.localization.spectrum.formulas;

public class BarinelFormula implements Formula{

    public BarinelFormula(){
    }

    @Override
    public double compute(int nPassingNotExecuting, int nFailingNotExecuting, int nPassingExecuting, int nFailingExecuting) {
        if(nPassingExecuting + nFailingExecuting == 0){
            return 0;
        }
        return 1.0 - ((double)nPassingExecuting / (double)(nPassingExecuting + nFailingExecuting));
    }
}
