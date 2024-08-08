package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Member;
import com.example.demo.model.MemberRepository;

@Service
public class MemberService{

	@Autowired
	private MemberRepository memberRepository;
	
	public List<Member> getAll(){
		return memberRepository.findAll();
	}
	
	public Member getOneMember(String username) {
		Member member = memberRepository.findByUsername(username);
		return member;
	}
	
}
