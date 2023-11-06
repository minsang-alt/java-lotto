package lotto;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LottoResultNotifier {

    private final Lottos lottos;

    private final LottoDraw lottoDraw;

    private final LottoResult[] lottoResults = LottoResult.values();

    private Map<LottoResult, Integer> totalWinningResult;

    public LottoResultNotifier(Lottos lottos, LottoDraw lottoDraw) {
        init();
        calculateResult(lottos, lottoDraw);

        this.lottos = lottos;
        this.lottoDraw = lottoDraw;
    }


    public Map<LottoResult, Integer> getResultMap() {
        return Collections.unmodifiableMap(totalWinningResult);
    }

    public long getTotalWinningAmount() {
        long totalMoney = 0L;
        Set<LottoResult> keySet = totalWinningResult.keySet();

        for (LottoResult lottoResult : keySet) {
            totalMoney += lottoResult.getAmount() * totalWinningResult.get(lottoResult);
        }

        return totalMoney;

    }

    public String getRateOfReturn(int purchaseAmount) {
        long winningAmount = getTotalWinningAmount();
        try {
            double rateOfReturn = ((double) winningAmount / purchaseAmount) * 100.0;
            return String.format("%.1f", rateOfReturn);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("로또는 최소 1개이상 구입해야 합니다.");
        }
    }


    private void init() {
        this.totalWinningResult = new EnumMap<>(LottoResult.class);

        for (LottoResult lottoResult : lottoResults) {
            totalWinningResult.put(lottoResult, totalWinningResult.getOrDefault(lottoResult, 0));
        }
    }

    private void calculateResult(Lottos lottos, LottoDraw lottoDraw) {
        List<Lotto> lottoBundle = lottos.lottoBundle();
        for (Lotto lotto : lottoBundle) {
            LottoResult result = LottoResult.calculateResult(lotto, lottoDraw);
            totalWinningResult.put(result, totalWinningResult.getOrDefault(result, 0) + 1);
        }

    }


}
