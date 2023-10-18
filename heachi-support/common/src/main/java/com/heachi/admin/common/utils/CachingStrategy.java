package com.heachi.admin.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
public class CachingStrategy {

    /**
     * Caching을 위한 메소드 t1을 넣어 T2 객체를 받는다.
     * f1은 캐싱이 되어있는지 확인해서 되어있다면, 가져오는 메소드
     * f2는 f1에서 가져오지 못했을때 (캐싱이 안되어있는 경우), f2를 실행시킨다.
     *
     * @param t1 f1, f2에 사용할 Parameter
     * @param f1 t1을 받아 실행시킬 람다식
     * @param f2 f1이 실패했을때, t1을 받아 실행시킬 람다식
     * @param <T1> t1의 객체
     * @param <T2> t1을 f1이나 f2에 넣었을때 리턴하는 객체
     *
     * @return
     */
    public static <T1,T2> T2 cachingIfEmpty(T1 t1,
                                            Function<T1, T2> f1,
                                            Function<T1, T2> f2) {
        try {
            T2 result = f1.apply(t1); // f1에 t1을 넣고 실행

            // 만약, result가 null이라면,
            if (result == null) {
                return f2.apply(t1);
            } else {
                return result;
            }
        } catch (RuntimeException e) {
            log.warn(">>>> Caching 과정에서 오류가 발생했습니다.");

            throw new RuntimeException();
        }
    }

    /**
     * Caching을 위한 메소드 t1을 넣어 T2 객체를 받는다.
     * f1은 캐싱이 되어있는지 확인해서 되어있다면, 가져오는 메소드
     * f2는 f1에서 가져오지 못했을때 (캐싱이 안되어있는 경우) or 조건에 부합하지 않는 경우, f2를 실행시킨다.
     *
     * @param t1 f1, f2에 사용할 Parameter
     * @param f1 t1을 받아 실행시킬 람다식
     * @param f2 f1이 실패했을때, t1을 받아 실행시킬 람다식
     * @param conditional f1의 리턴값인 T2가 조건을 만족하지 않는다면, f2를 실행시킴
     * @param <T1> t1의 객체
     * @param <T2> t1을 f1이나 f2에 넣었을때 리턴하는 객체
     *
     * @return
     */
    public static <T1,T2> T2 cachingIfEmpty(T1 t1,
                                            Function<T1, T2> f1,
                                            Function<T1, T2> f2,
                                            Predicate<T2> conditional) {
        try {
            T2 result = f1.apply(t1); // f1에 t1을 넣고 실행

            // 만약, result가 null이거나, 조건에 부합하지 않으면
            if (result == null || !conditional.test(result)) {
                return f2.apply(t1);
            } else {
                return result;
            }
        } catch (RuntimeException e) {
            log.warn(">>>> Caching 과정에서 오류가 발생했습니다.");

            throw new RuntimeException();
        }
    }
}
