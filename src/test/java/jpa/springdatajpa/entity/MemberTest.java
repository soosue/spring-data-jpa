package jpa.springdatajpa.entity;

import jpa.springdatajpa.repository.MemberRepository;
import jpa.springdatajpa.repository.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(readOnly = true)
@Rollback(false)
class MemberTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext EntityManager em;

    @Test
    @Transactional
    public void auditingTest() {
        //BaseEntity test
        Member member1 = new Member("nameA");
        memberRepository.save(member1);

        //BaseTimeEntity test
        Team team2 = new Team("teamB");
        teamRepository.save(team2);


        assertThat(member1.getCreatedDate()).isEqualTo(member1.getLastModifiedDate());
        assertThat(member1.getCreatedBy()).isEqualTo(member1.getLastModifiedBy());
        assertThat(team2.getCreatedDate()).isEqualTo(team2.getLastModifiedDate());

        Member findedMember = memberRepository.findById(member1.getId()).orElse(null);
        findedMember.changName("nameB");

        em.flush();

        assertThat(findedMember.getCreatedDate()).isNotEqualTo(findedMember.getLastModifiedDate());

    }

}