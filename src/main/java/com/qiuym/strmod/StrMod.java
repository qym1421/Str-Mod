package com.qiuym.strmod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
// import net.minecraft.world.food.FoodProperties;  // 注释：不需要食物
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
// import net.minecraftforge.fml.config.ModConfig;  // 注释：不需要配置
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(StrMod.MODID)
public class StrMod
{
    public static final String MODID = "strmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    // ========== 方块注册 ==========
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    // 注册示例方块
    public static final RegistryObject<Block> QUARTZ_GLASS = BLOCKS.register("quartz_glass",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));

    // ========== 物品注册 ==========
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    // 注册方块对应的物品
    public static final RegistryObject<Item> QUARTZ_GLASS_ITEM = ITEMS.register("quartz_glass",
            () -> new BlockItem(QUARTZ_GLASS.get(), new Item.Properties()));

    // 注册普通物品（示例）
    /* public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item",
            () -> new Item(new Item.Properties()));
    */

    /* ========== 注释：不需要食物系统 ==========
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(1).saturationMod(2f).build())));
    */

    /* ========== 注释：不需要自定义创造标签页 ==========
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get());
            }).build());
    */

    public StrMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // 注册通用设置
        modEventBus.addListener(this::commonSetup);

        // 注册方块和物品
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        // CREATIVE_MODE_TABS.register(modEventBus);  // 注释：不需要自定义标签页

        // 注册事件总线
        MinecraftForge.EVENT_BUS.register(this);

        // 添加物品到原版创造标签页
        modEventBus.addListener(this::addCreative);

        // context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);  // 注释：不需要配置文件
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        /* ========== 注释：不需要配置相关日志 ==========
        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
        */
    }

    // 将方块物品添加到建筑方块标签页
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        // 添加示例方块到建筑方块标签页
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(QUARTZ_GLASS_ITEM);

        // 添加示例物品到工具与实用物品标签页（可选）
        // if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES)
        //     event.accept(EXAMPLE_ITEM);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("Str in Server!");
    }

    // 客户端事件
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                Block glassBlock = QUARTZ_GLASS.get();

                ItemBlockRenderTypes.setRenderLayer(glassBlock, RenderType.cutout());
            });
            LOGGER.info("STR HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}