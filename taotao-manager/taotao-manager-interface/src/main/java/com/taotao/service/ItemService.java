package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

/**
 * 商品相关的处理的service
 * @title ItemService.java
 * <p>description</p>
 * <p>company: www.itheima.com</p>
 * @author ljh 
 * @version 1.0
 */
public interface ItemService {
	
	/**
	 *  根据当前的页码和每页 的行数进行分页查询
	 * @param page
	 * @param rows
	 * @return
	 */
	public EasyUIDataGridResult getItemList(Integer page, Integer rows);

    /**
     * 新增商品
     * @param item
     * @param desc
     * @return
     */
	public TaotaoResult saveItem(TbItem item, String desc);

	/**
	 * 根据id查询商品
	 * @param itemId
	 * @return
	 */
	public TbItem getItemById(Long itemId);

	/**
	 * 根据id查询商品描述
	 * @param itemId
	 * @return
	 */
	public TbItemDesc getItemDescById(Long itemId);
}
