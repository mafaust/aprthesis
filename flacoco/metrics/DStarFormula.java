package fr.spoonlabs.flacoco.localization.spectrum.formulas;

public class DStarFormula implements Formula {

    public DStarFormula(){
    }

    @Override
    public double compute(int nPassingNotExecuting, int nFailingNotExecuting, int nPassingExecuting, int nFailingExecuting) {
        if((nFailingNotExecuting + nPassingExecuting)== 0 || (nFailingExecuting == 0) ){
            return 0;
        }

        return Math.pow(nFailingExecuting, 2) / (double)(nFailingNotExecuting + nPassingExecuting);
    }
}
