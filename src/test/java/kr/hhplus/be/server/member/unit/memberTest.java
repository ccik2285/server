package kr.hhplus.be.server.member.unit;

import kr.hhplus.be.server.domain.member.repository.MemberPointRepositoryCustom;
import kr.hhplus.be.server.domain.member.service.MemberPointService;
import kr.hhplus.be.server.domain.member.usecase.ChargeBalanceUseCase;
import kr.hhplus.be.server.domain.member.usecase.GetBalanceUseCase;
import kr.hhplus.be.server.domain.member.usecase.UseBalanceUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class memberTest {

    @Mock
    private MemberPointRepositoryCustom memberPointRepositoryCustom;

    @InjectMocks
    private GetBalanceUseCase getBalanceUseCase;

    @InjectMocks
    private ChargeBalanceUseCase chargeBalanceUseCase;

    @InjectMocks
    private UseBalanceUseCase useBalanceUseCase;

    @BeforeEach
    void setUp() {
        useBalanceUseCase = new UseBalanceUseCase(memberPointRepositoryCustom, getBalanceUseCase);
    }
    @Test
    void 잔액조회테스트() {
        Long mbrNo = 1L;
        Long expectedBalance = 1000L;

        when(memberPointRepositoryCustom.getBalance(mbrNo)).thenReturn(expectedBalance);
        Long balance = getBalanceUseCase.execute(mbrNo);

        assertThat(balance).isEqualTo(expectedBalance);

        verify(memberPointRepositoryCustom).getBalance(mbrNo);
    }

    @Test
    void 잔액충전테스트() {
        Long mbrNo = 1L;
        Long amount = 500L;

        chargeBalanceUseCase.execute(mbrNo, amount);

        verify(memberPointRepositoryCustom).chargeBalance(mbrNo, amount);
    }

    @Test
    void 충전실패_옳바르지않은금액() {
        Long mbrNo = 1L;
        Long invalidAmount = -100L;

        assertThatThrownBy(() -> chargeBalanceUseCase.execute(mbrNo, invalidAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("충전 금액은 0보다 커야 합니다.");

        verify(memberPointRepositoryCustom, never()).chargeBalance(any(), any());
    }

    @Test
    void 포인트사용() {
        Long mbrNo = 1L;
        Long amount = 150L;
        Long currentBalance = 100L;

        when(getBalanceUseCase.execute(mbrNo)).thenReturn(currentBalance);

        try {
            useBalanceUseCase.execute(mbrNo, amount);
        } catch (IllegalArgumentException e) {
            assertEquals("사용할 수 있는 포인트가 부족합니다.", e.getMessage());
        }
    }
}
