package com.example.demo2.algorithms.bwt;

import com.example.demo2.algorithmDisplays.WindowManager;
import com.example.demo2.algorithmDisplays.SelectorDisplay;
import com.example.demo2.algorithmDisplays.DisplayType;
import com.example.demo2.algorithmManager.AlgorithmManager;
import com.example.demo2.algorithmManager.AlgorithmType;
import com.example.demo2.algorithms.Algorithm;

public class BWTDivisionAlgorithm extends Algorithm {

    public BWTDivisionAlgorithm(AlgorithmManager algorithmManager) {
        super(algorithmManager);

        SelectorDisplay selectorDisplay1 = (SelectorDisplay)
                WindowManager.addDisplay(DisplayType.Selector, "", 1);
        if (selectorDisplay1 != null) {
            selectorDisplay1.setChoice("introduction", "bwtIntroductionDescription", AlgorithmType.BWTEncode, algorithmManager );
        }

        SelectorDisplay selectorDisplay2 = (SelectorDisplay)
                WindowManager.addDisplay(DisplayType.Selector, "", 1);
        if (selectorDisplay2 != null) {
            selectorDisplay2.setChoice("inverseBWT", "inverseBWTDescription", AlgorithmType.BWTDecode, algorithmManager);
        }

        SelectorDisplay selectorDisplay3 = (SelectorDisplay)
                WindowManager.addDisplay(DisplayType.Selector, "", 1);
        if (selectorDisplay3 != null) {
            selectorDisplay3.setChoice("BWTUsingSA", "BWTUsingSADescription", AlgorithmType.BWTFromSA, algorithmManager);
        }

        SelectorDisplay selectorDisplay4 = (SelectorDisplay)
                WindowManager.addDisplay(DisplayType.Selector, "", 1);
        if (selectorDisplay4 != null) {
            selectorDisplay4.setChoice("patternMatching", "BWTPatternMatchingDescription", AlgorithmType.BWTSearch, algorithmManager);
        }
    }
}
