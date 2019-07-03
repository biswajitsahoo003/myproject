package com.qcm.hotel.portal;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
public static void main(String a[]) {
	String[] s= {"fgfg","hghg"};
	List<String> dd = Arrays.stream(s).map(test -> "-"+ test).collect(Collectors.toList());
	System.out.println( String.join(",", dd));
}
}
