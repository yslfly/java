package com.uid.utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 业务结算工具类
 * @author Administrator
 *
 */
public class CalculateUtils {
	
	
	/**
	 * 计算顺丰运费
	 * @param shipping_config
	 * @param goods_weight   商品总的重量
	 * @param goods_number   商品总的件数
	 * @return
	 */
		@SuppressWarnings("unchecked")
		public static  double sf_shipping_fee(String shipping_config, double goods_weight,
				int goods_number){
		    goods_weight = goods_weight*1000;
			HashMap<String,String>configsMap  = getShippingConfigMap(shipping_config);
			float item_fee = Float.parseFloat(configsMap.get("item_fee"));//单件商品的配送费用
			float base_fee = Float.parseFloat(configsMap.get("base_fee"));//1000克以内的价格
			float step_fee = Float.parseFloat(configsMap.get("step_fee"));//续重每1000克增加的价格
			String fee_compute_mode =configsMap.get("fee_compute_mode");//运费计算模式:按件数,按重量
			
			double shippingFee;
			if("by_weight".equals(fee_compute_mode)){
				if(goods_weight > 1000){//商品总重量大于1000克,多出的部分用续重运费来算
					shippingFee = base_fee + ((goods_weight - 1000)/1000)*step_fee;
				}else{
					shippingFee = base_fee;
				}
			}else{
				shippingFee = goods_number*item_fee;
			}
		    return shippingFee;
		}
		
		
		/**
		 * 默认方式计算顺丰运费
		 * 首重20元/KG，续重2元/KG
		 * @param shipping_config
		 * @param goods_weight   商品总的重量
		 * @return
		 */
			public static  double sf_shipping_fee_default(double goods_weight){
				goods_weight = goods_weight*1000;
				double shippingFee;			
				if(goods_weight > 1000){//商品总重量大于1000克,多出的部分用续重运费来算
					shippingFee = 20 + ((goods_weight - 1000)/1000)*2;
				}else{
					shippingFee = 20;
				}
			
			    return shippingFee;
			}
			
			public static double flat_shipping_fee(String shipping_config){
				HashMap<String,String> configsMap  = getShippingConfigMap(shipping_config);		
				return Double.parseDouble(configsMap.get("base_fee"));
			}
			
			
			public static HashMap<String,String> getShippingConfigMap(String shipping_config){
				ArrayList<HashMap<String,String>> shippingConfigList = null;		
				try {
					//解析存放在数据库中的php序列化配送收费规则数据
					shippingConfigList = (ArrayList<HashMap<String,String>>) PHPSerializableUtils
							.unserialize(shipping_config.getBytes());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				HashMap<String,String> configsMap = new HashMap<String,String> ();
				System.out.println("--------------------get shipping_config start----------------");
				if(shippingConfigList != null){
					for(HashMap<String,String> configMap:shippingConfigList){
						System.out.println("name:"+configMap.get("name")+"\t value:"+configMap.get("value"));					
						configsMap.put(configMap.get("name"),configMap.get("value"));
					}			
				}
				System.out.println("--------------------shipping_config  over----------------");
				return configsMap;
			}

}
