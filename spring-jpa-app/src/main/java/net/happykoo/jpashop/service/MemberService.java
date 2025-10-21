package net.happykoo.jpashop.service;

import lombok.RequiredArgsConstructor;
import net.happykoo.jpashop.domain.Member;
import net.happykoo.jpashop.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;

    public Long join(Member member) {
        validateDuplicateMember(member);
        repository.save(member);
        return member.getId();
    }

    public List<Member> findMembers() {
        return repository.findAll();
    }

    public Member findOne(Long memberId) {
        return repository.findOne(memberId);
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = repository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}
