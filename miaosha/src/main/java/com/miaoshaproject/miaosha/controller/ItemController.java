package com.miaoshaproject.miaosha.controller;

import com.miaoshaproject.miaosha.controller.viewobject.ItemVO;
import com.miaoshaproject.miaosha.error.BusinessException;
import com.miaoshaproject.miaosha.response.CommonReturnType;
import com.miaoshaproject.miaosha.service.ItemService;
import com.miaoshaproject.miaosha.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller("/item")
@RequestMapping("/item")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    //创建商品的Controller
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = CONTENT_TYPE_FORMED)
    public CommonReturnType createItem(@RequestParam(name="title") String title,
                                       @RequestParam(name="description") String description,
                                       @RequestParam(name="price")BigDecimal price,
                                       @RequestParam(name="stock")Integer stock,
                                       @RequestParam(name="imgUrl")String imgUrl) throws BusinessException {
        //封装service请求用来创建商品
        ItemModel itemModel=new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setImgUrl(imgUrl);
        itemModel.setStock(stock);

        ItemModel itemModelForReturn=itemService.createItem(itemModel);
        ItemVO itemVO=new ItemVO();
        itemVO=convertVOFromModel(itemModelForReturn);
        CommonReturnType commonReturnType= CommonReturnType.create(itemVO);
        return commonReturnType;
    }

    @RequestMapping(value = "/cre", method = RequestMethod.GET)
    public String createItem(){
        return "createitem.html";
    }

    @RequestMapping(value = "/pre", method = RequestMethod.GET)
    public String presentList(){
        return "listitem.html";
    }

    @RequestMapping(value = "/preOne", method = RequestMethod.GET)
    public String presentOne(@RequestParam(name="id")Integer id){
        return "getitem.html";
    }



    private ItemVO convertVOFromModel(ItemModel itemModel){
        ItemVO itemVO=new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        if(itemModel.getPromoModel()!=null){
            //有正在进行的或者即将进行的秒杀活动
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else{
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }


    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id){
        ItemModel itemModel=itemService.getItemById(id);
        ItemVO itemVO=convertVOFromModel(itemModel);
        return CommonReturnType.create(itemVO);
    }


    //商品列表页面浏览
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonReturnType listItem(){
        List<ItemModel> itemModelList=itemService.listItem();
        List<ItemVO> itemVOList=itemModelList.stream().map(itemModel -> {
            ItemVO itemVO=convertVOFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVOList);

    }
}
