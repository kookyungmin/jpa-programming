package net.happykoo.jpa.ch9;

import net.happykoo.jpa.PersistenceContextHandler;
import net.happykoo.jpa.ch9.embedded.Address;
import net.happykoo.jpa.ch9.embedded.ZipCode;
import net.happykoo.jpa.ch9.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ValueTypeTest {
    @Test
    @DisplayName("Embedded :: 값 타입 테스트")
    public void test1() {
        try(PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction((em, tx) -> {
                Address address = Address.builder()
                        .city("Seoul")
                        .street("Nangok")
                        .zipCode(ZipCode.builder()
                                .zip("111")
                                .plusFour("333")
                                .build())
                        .build();
                Member member = Member.builder()
                        .name("marco")
                        .age(33)
                        .homeAddress(address)
                        .build();

                em.persist(member);
            });
        }
    }

    @Test
    @DisplayName("Embedded :: 값 타입 복사 테스트")
    public void test2() {
        try(PersistenceContextHandler pc = new PersistenceContextHandler("happykoo")) {
            pc.runTransaction((em, tx) -> {
                Address address = Address.builder()
                        .city("Seoul")
                        .street("Nangok")
                        .zipCode(ZipCode.builder()
                                .zip("111")
                                .plusFour("333")
                                .build())
                        .build();
                Member m1 = Member.builder()
                        .name("marco")
                        .age(33)
                        .homeAddress(address)
                        .build();

                em.persist(m1);

                //Address 공유하려면 clone 이용 -> 만약, 그대로 공유하면 address 를 변경하면 m1 도 update 됨
                //Address 를 불변객체로 만드는게 가장 좋음 (setter 제공 X, (다른곳에서 수정 못함)) -> 부작용을 줄이는 방법
                Address newAddress = address.clone();
                newAddress.setCity("New York");
                Member m2 = Member.builder()
                        .name("happykoo")
                        .age(33)
                        .homeAddress(newAddress)
                        .build();
                em.persist(m2);

                assertNotEquals(m1.getHomeAddress().getCity(), m2.getHomeAddress().getCity());
            });
        }
    }
}
