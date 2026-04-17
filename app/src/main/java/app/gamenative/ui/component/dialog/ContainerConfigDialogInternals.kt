package app.gamenative.ui.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.gamenative.R
import app.gamenative.ui.component.dialog.state.MessageDialogState
import app.gamenative.utils.ContainerUtils
import app.gamenative.utils.ManifestComponentHelper
import app.gamenative.utils.ManifestContentTypes
import app.gamenative.utils.ManifestData
import app.gamenative.utils.ManifestEntry
import com.winlator.box86_64.Box86_64Preset
import com.winlator.box86_64.Box86_64PresetManager
import com.winlator.container.Container
import com.winlator.container.ContainerData
import com.winlator.core.GPUHelper
import com.winlator.fexcore.FEXCorePreset
import com.winlator.fexcore.FEXCorePresetManager

internal class ContainerConfigResources(
    val screenSizes: List<String>,
    val baseGraphicsDrivers: List<String>,
    val dxWrappers: List<String>,
    val dxvkVersionsBase: List<String>,
    val vkd3dVersionsBase: List<String>,
    val audioDrivers: List<String>,
    val gpuCards: Map<Int, ContainerUtils.GpuInfo>,
    val presentModes: List<String>,
    val resourceTypes: List<String>,
    val bcnEmulationEntries: List<String>,
    val bcnEmulationTypeEntries: List<String>,
    val sharpnessEffects: List<String>,
    val sharpnessDisplayItems: List<String>,
    val renderingModes: List<String>,
    val videoMemSizes: List<String>,
    val mouseWarps: List<String>,
    val externalDisplayModes: List<String>,
    val winCompOpts: List<String>,
    val box64Versions: List<String>,
    val wowBox64VersionsBase: List<String>,
    val box64BionicVersionsBase: List<String>,
    val box64Presets: List<Box86_64Preset>,
    val fexcoreVersionsBase: List<String>,
    val fexcorePresets: List<FEXCorePreset>,
    val fexcoreTSOPresets: List<String>,
    val fexcoreX87Presets: List<String>,
    val fexcoreMultiblockValues: List<String>,
    val startupSelectionEntries: List<String>,
    val turnipVersions: List<String>,
    val virglVersions: List<String>,
    val zinkVersions: List<String>,
    val vortekVersions: List<String>,
    val adrenoVersions: List<String>,
    val sd8EliteVersions: List<String>,
    val containerVariants: List<String>,
    val bionicWineEntriesBase: List<String>,
    val glibcWineEntriesBase: List<String>,
    val emulatorEntries: List<String>,
    val bionicGraphicsDrivers: List<String>,
    val baseWrapperVersions: List<String>,
    val languages: List<String>,
    val dxvkOptions: ManifestComponentHelper.VersionOptionList,
    val vkd3dOptions: ManifestComponentHelper.VersionOptionList,
    val box64Options: ManifestComponentHelper.VersionOptionList,
    val box64BionicOptions: ManifestComponentHelper.VersionOptionList,
    val wowBox64Options: ManifestComponentHelper.VersionOptionList,
    val fexcoreOptions: ManifestComponentHelper.VersionOptionList,
    val wrapperOptions: ManifestComponentHelper.VersionOptionList,
    val bionicWineOptions: ManifestComponentHelper.VersionOptionList,
    val glibcWineOptions: ManifestComponentHelper.VersionOptionList,
    val dxvkManifestById: Map<String, ManifestEntry>,
    val vkd3dManifestById: Map<String, ManifestEntry>,
    val box64ManifestById: Map<String, ManifestEntry>,
    val wowBox64ManifestById: Map<String, ManifestEntry>,
    val fexcoreManifestById: Map<String, ManifestEntry>,
    val wrapperManifestById: Map<String, ManifestEntry>,
    val bionicWineManifestById: Map<String, ManifestEntry>,
    val glibcWineManifestById: Map<String, ManifestEntry>,
    val gpuExtensions: List<String>,
    val inspectionMode: Boolean,
    val isBionicVariant: Boolean,
)

@Composable
internal fun rememberContainerConfigResources(
    context: android.content.Context,
    config: ContainerData,
    componentAvailability: ManifestComponentHelper.ComponentAvailability?,
): ContainerConfigResources {
    val screenSizes = stringArrayResource(R.array.screen_size_entries).toList()
    val baseGraphicsDrivers = stringArrayResource(R.array.graphics_driver_entries).toList()
    val dxWrappers = stringArrayResource(R.array.dxwrapper_entries).toList()
    val dxvkVersionsBase = stringArrayResource(R.array.dxvk_version_entries).toList()
    val vkd3dVersionsBase = stringArrayResource(R.array.vkd3d_version_entries).toList()
    val audioDrivers = stringArrayResource(R.array.audio_driver_entries).toList()
    val presentModes = stringArrayResource(R.array.present_mode_entries).toList()
    val resourceTypes = stringArrayResource(R.array.resource_type_entries).toList()
    val bcnEmulationEntries = stringArrayResource(R.array.bcn_emulation_entries).toList()
    val bcnEmulationTypeEntries = stringArrayResource(R.array.bcn_emulation_type_entries).toList()
    val sharpnessEffects = stringArrayResource(R.array.vkbasalt_sharpness_entries).toList()
    val sharpnessEffectLabels = stringArrayResource(R.array.vkbasalt_sharpness_labels).toList()
    val sharpnessDisplayItems =
        if (sharpnessEffectLabels.size == sharpnessEffects.size) sharpnessEffectLabels else sharpnessEffects
    val renderingModes = stringArrayResource(R.array.offscreen_rendering_modes).toList()
    val videoMemSizes = stringArrayResource(R.array.video_memory_size_entries).toList()
    val mouseWarps = stringArrayResource(R.array.mouse_warp_override_entries).toList()
    val externalDisplayModes = listOf(
        stringResource(R.string.external_display_mode_off),
        stringResource(R.string.external_display_mode_touchpad),
        stringResource(R.string.external_display_mode_keyboard),
        stringResource(R.string.external_display_mode_hybrid),
    )
    val winCompOpts = stringArrayResource(R.array.win_component_entries).toList()
    val box64Versions = stringArrayResource(R.array.box64_version_entries).toList()
    val wowBox64VersionsBase = stringArrayResource(R.array.wowbox64_version_entries).toList()
    val box64BionicVersionsBase = stringArrayResource(R.array.box64_bionic_version_entries).toList()
    val fexcoreVersionsBase = stringArrayResource(R.array.fexcore_version_entries).toList()
    val fexcoreTSOPresets = stringArrayResource(R.array.fexcore_preset_entries).toList()
    val fexcoreX87Presets = stringArrayResource(R.array.x87mode_preset_entries).toList()
    val fexcoreMultiblockValues = stringArrayResource(R.array.multiblock_values).toList()
    val startupSelectionEntries = stringArrayResource(R.array.startup_selection_entries).toList()
    val turnipVersions = stringArrayResource(R.array.turnip_version_entries).toList()
    val virglVersions = stringArrayResource(R.array.virgl_version_entries).toList()
    val zinkVersions = stringArrayResource(R.array.zink_version_entries).toList()
    val vortekVersions = stringArrayResource(R.array.vortek_version_entries).toList()
    val adrenoVersions = stringArrayResource(R.array.adreno_version_entries).toList()
    val sd8EliteVersions = stringArrayResource(R.array.sd8elite_version_entries).toList()
    val containerVariants = stringArrayResource(R.array.container_variant_entries).toList()
    val bionicWineEntriesBase = stringArrayResource(R.array.bionic_wine_entries).toList()
    val glibcWineEntriesBase = stringArrayResource(R.array.glibc_wine_entries).toList()
    val emulatorEntries = stringArrayResource(R.array.emulator_entries).toList()
    val bionicGraphicsDrivers = stringArrayResource(R.array.bionic_graphics_driver_entries).toList()
    val baseWrapperVersions = stringArrayResource(R.array.wrapper_graphics_driver_version_entries).toList()
    val languages = listOf(
        "arabic", "bulgarian", "schinese", "tchinese", "czech", "danish", "dutch", "english",
        "finnish", "french", "german", "greek", "hungarian", "italian", "japanese", "koreana",
        "norwegian", "polish", "portuguese", "brazilian", "romanian", "russian", "spanish",
        "latam", "swedish", "thai", "turkish", "ukrainian", "vietnamese",
    )

    val gpuCards = remember(context) { ContainerUtils.getGPUCards(context) }
    val box64Presets = remember(context) { Box86_64PresetManager.getPresets("box64", context) }
    val fexcorePresets = remember(context) { FEXCorePresetManager.getPresets(context) }
    val inspectionMode = LocalInspectionMode.current
    val gpuExtensions = remember(inspectionMode) {
        if (inspectionMode) {
            listOf("VK_KHR_swapchain", "VK_KHR_maintenance1", "VK_KHR_timeline_semaphore")
        } else {
            GPUHelper.vkGetDeviceExtensions().toList()
        }
    }

    val manifestData = componentAvailability?.manifest ?: ManifestData.empty()
    val installedLists = componentAvailability?.installed
    val isBionicVariant = config.containerVariant.equals(Container.BIONIC, ignoreCase = true)

    val manifestDxvk = manifestData.items[ManifestContentTypes.DXVK].orEmpty()
    val manifestVkd3d = manifestData.items[ManifestContentTypes.VKD3D].orEmpty()
    val manifestBox64 = manifestData.items[ManifestContentTypes.BOX64].orEmpty()
    val manifestWowBox64 = manifestData.items[ManifestContentTypes.WOWBOX64].orEmpty()
    val manifestFexcore = manifestData.items[ManifestContentTypes.FEXCORE].orEmpty()
    val manifestDrivers = manifestData.items[ManifestContentTypes.DRIVER].orEmpty()
    val manifestWine = manifestData.items[ManifestContentTypes.WINE].orEmpty()
    val manifestProton = manifestData.items[ManifestContentTypes.PROTON].orEmpty()

    val installedDxvk = installedLists?.dxvk.orEmpty()
    val installedVkd3d = installedLists?.vkd3d.orEmpty()
    val installedBox64 = installedLists?.box64.orEmpty()
    val installedWowBox64 = installedLists?.wowBox64.orEmpty()
    val installedFexcore = installedLists?.fexcore.orEmpty()
    val installedWine = installedLists?.wine.orEmpty()
    val installedProton = installedLists?.proton.orEmpty()
    val installedWrapperDrivers = componentAvailability?.installedDrivers.orEmpty()

    val dxvkOptions = remember(dxvkVersionsBase, installedDxvk, manifestDxvk) {
        ManifestComponentHelper.buildVersionOptionList(dxvkVersionsBase, installedDxvk, manifestDxvk)
    }
    val vkd3dOptions = remember(vkd3dVersionsBase, installedVkd3d, manifestVkd3d) {
        ManifestComponentHelper.buildVersionOptionList(vkd3dVersionsBase, installedVkd3d, manifestVkd3d)
    }
    val box64Options = remember(box64Versions, installedBox64, manifestBox64) {
        ManifestComponentHelper.buildVersionOptionList(box64Versions, installedBox64, manifestBox64)
    }
    val box64BionicOptions = remember(box64BionicVersionsBase, installedBox64, manifestBox64) {
        ManifestComponentHelper.buildVersionOptionList(box64BionicVersionsBase, installedBox64, manifestBox64)
    }
    val wowBox64Options = remember(wowBox64VersionsBase, installedWowBox64, manifestWowBox64) {
        ManifestComponentHelper.buildVersionOptionList(wowBox64VersionsBase, installedWowBox64, manifestWowBox64)
    }
    val fexcoreOptions = remember(fexcoreVersionsBase, installedFexcore, manifestFexcore) {
        ManifestComponentHelper.buildVersionOptionList(fexcoreVersionsBase, installedFexcore, manifestFexcore)
    }
    val wrapperOptions = remember(baseWrapperVersions, installedWrapperDrivers, manifestDrivers) {
        ManifestComponentHelper.buildVersionOptionList(baseWrapperVersions, installedWrapperDrivers, manifestDrivers)
    }

    val bionicWineManifest = remember(manifestWine, manifestProton) {
        ManifestComponentHelper.filterManifestByVariant(manifestWine, "bionic") +
            ManifestComponentHelper.filterManifestByVariant(manifestProton, "bionic")
    }
    val glibcWineManifest = remember(manifestWine, manifestProton) {
        ManifestComponentHelper.filterManifestByVariant(manifestWine, "glibc") +
            ManifestComponentHelper.filterManifestByVariant(manifestProton, "glibc")
    }
    val bionicWineOptions = remember(bionicWineEntriesBase, installedWine, installedProton, bionicWineManifest) {
        ManifestComponentHelper.buildVersionOptionList(
            bionicWineEntriesBase,
            installedWine + installedProton,
            bionicWineManifest,
        )
    }
    val glibcWineOptions = remember(glibcWineEntriesBase, glibcWineManifest) {
        ManifestComponentHelper.buildVersionOptionList(glibcWineEntriesBase, emptyList(), glibcWineManifest)
    }

    val dxvkManifestById = remember(manifestDxvk) { manifestDxvk.associateBy { it.id } }
    val vkd3dManifestById = remember(manifestVkd3d) { manifestVkd3d.associateBy { it.id } }
    val box64ManifestById = remember(manifestBox64) { manifestBox64.associateBy { it.id } }
    val wowBox64ManifestById = remember(manifestWowBox64) { manifestWowBox64.associateBy { it.id } }
    val fexcoreManifestById = remember(manifestFexcore) { manifestFexcore.associateBy { it.id } }
    val wrapperManifestById = remember(manifestDrivers) { manifestDrivers.associateBy { it.id } }
    val bionicWineManifestById = remember(bionicWineManifest) { bionicWineManifest.associateBy { it.id } }
    val glibcWineManifestById = remember(glibcWineManifest) { glibcWineManifest.associateBy { it.id } }

    return ContainerConfigResources(
        screenSizes = screenSizes,
        baseGraphicsDrivers = baseGraphicsDrivers,
        dxWrappers = dxWrappers,
        dxvkVersionsBase = dxvkVersionsBase,
        vkd3dVersionsBase = vkd3dVersionsBase,
        audioDrivers = audioDrivers,
        gpuCards = gpuCards,
        presentModes = presentModes,
        resourceTypes = resourceTypes,
        bcnEmulationEntries = bcnEmulationEntries,
        bcnEmulationTypeEntries = bcnEmulationTypeEntries,
        sharpnessEffects = sharpnessEffects,
        sharpnessDisplayItems = sharpnessDisplayItems,
        renderingModes = renderingModes,
        videoMemSizes = videoMemSizes,
        mouseWarps = mouseWarps,
        externalDisplayModes = externalDisplayModes,
        winCompOpts = winCompOpts,
        box64Versions = box64Versions,
        wowBox64VersionsBase = wowBox64VersionsBase,
        box64BionicVersionsBase = box64BionicVersionsBase,
        box64Presets = box64Presets,
        fexcoreVersionsBase = fexcoreVersionsBase,
        fexcorePresets = fexcorePresets,
        fexcoreTSOPresets = fexcoreTSOPresets,
        fexcoreX87Presets = fexcoreX87Presets,
        fexcoreMultiblockValues = fexcoreMultiblockValues,
        startupSelectionEntries = startupSelectionEntries,
        turnipVersions = turnipVersions,
        virglVersions = virglVersions,
        zinkVersions = zinkVersions,
        vortekVersions = vortekVersions,
        adrenoVersions = adrenoVersions,
        sd8EliteVersions = sd8EliteVersions,
        containerVariants = containerVariants,
        bionicWineEntriesBase = bionicWineEntriesBase,
        glibcWineEntriesBase = glibcWineEntriesBase,
        emulatorEntries = emulatorEntries,
        bionicGraphicsDrivers = bionicGraphicsDrivers,
        baseWrapperVersions = baseWrapperVersions,
        languages = languages,
        dxvkOptions = dxvkOptions,
        vkd3dOptions = vkd3dOptions,
        box64Options = box64Options,
        box64BionicOptions = box64BionicOptions,
        wowBox64Options = wowBox64Options,
        fexcoreOptions = fexcoreOptions,
        wrapperOptions = wrapperOptions,
        bionicWineOptions = bionicWineOptions,
        glibcWineOptions = glibcWineOptions,
        dxvkManifestById = dxvkManifestById,
        vkd3dManifestById = vkd3dManifestById,
        box64ManifestById = box64ManifestById,
        wowBox64ManifestById = wowBox64ManifestById,
        fexcoreManifestById = fexcoreManifestById,
        wrapperManifestById = wrapperManifestById,
        bionicWineManifestById = bionicWineManifestById,
        glibcWineManifestById = glibcWineManifestById,
        gpuExtensions = gpuExtensions,
        inspectionMode = inspectionMode,
        isBionicVariant = isBionicVariant,
    )
}

internal fun buildContainerConfigState(
    resources: ContainerConfigResources,
    configState: androidx.compose.runtime.MutableState<ContainerData>,
    graphicsDriversRef: androidx.compose.runtime.MutableState<MutableList<String>>,
    bionicWineEntriesRef: androidx.compose.runtime.MutableState<List<String>>,
    glibcWineEntriesRef: androidx.compose.runtime.MutableState<List<String>>,
    wrapperVersionsRef: androidx.compose.runtime.MutableState<List<String>>,
    dxvkVersionsAllRef: androidx.compose.runtime.MutableState<List<String>>,
    componentAvailabilityRef: androidx.compose.runtime.MutableState<ManifestComponentHelper.ComponentAvailability?>,
    showCustomResolutionDialogRef: androidx.compose.runtime.MutableState<Boolean>,
    customResolutionValidationErrorRef: androidx.compose.runtime.MutableState<String?>,
    vkMaxVersionIndexRef: androidx.compose.runtime.MutableIntState,
    imageCacheIndexRef: androidx.compose.runtime.MutableIntState,
    exposedExtIndicesRef: androidx.compose.runtime.MutableState<List<Int>>,
    maxDeviceMemoryIndexRef: androidx.compose.runtime.MutableIntState,
    bionicDriverIndexRef: androidx.compose.runtime.MutableIntState,
    wrapperVersionIndexRef: androidx.compose.runtime.MutableIntState,
    presentModeIndexRef: androidx.compose.runtime.MutableIntState,
    resourceTypeIndexRef: androidx.compose.runtime.MutableIntState,
    bcnEmulationIndexRef: androidx.compose.runtime.MutableIntState,
    bcnEmulationTypeIndexRef: androidx.compose.runtime.MutableIntState,
    bcnEmulationCacheEnabledRef: androidx.compose.runtime.MutableState<Boolean>,
    disablePresentWaitCheckedRef: androidx.compose.runtime.MutableState<Boolean>,
    syncEveryFrameCheckedRef: androidx.compose.runtime.MutableState<Boolean>,
    sharpnessEffectIndexRef: androidx.compose.runtime.MutableIntState,
    sharpnessLevelRef: androidx.compose.runtime.MutableIntState,
    sharpnessDenoiseRef: androidx.compose.runtime.MutableIntState,
    adrenotoolsTurnipCheckedRef: androidx.compose.runtime.MutableState<Boolean>,
    emulator64IndexRef: androidx.compose.runtime.MutableIntState,
    emulator32IndexRef: androidx.compose.runtime.MutableIntState,
    screenSizeIndexRef: androidx.compose.runtime.MutableIntState,
    customScreenWidthRef: androidx.compose.runtime.MutableState<String>,
    customScreenHeightRef: androidx.compose.runtime.MutableState<String>,
    graphicsDriverIndexRef: androidx.compose.runtime.MutableIntState,
    dxWrapperIndexRef: androidx.compose.runtime.MutableIntState,
    dxvkVersionIndexRef: androidx.compose.runtime.MutableIntState,
    graphicsDriverVersionIndexRef: androidx.compose.runtime.MutableIntState,
    audioDriverIndexRef: androidx.compose.runtime.MutableIntState,
    gpuNameIndexRef: androidx.compose.runtime.MutableIntState,
    renderingModeIndexRef: androidx.compose.runtime.MutableIntState,
    videoMemIndexRef: androidx.compose.runtime.MutableIntState,
    mouseWarpIndexRef: androidx.compose.runtime.MutableIntState,
    externalDisplayModeIndexRef: androidx.compose.runtime.MutableIntState,
    languageIndexRef: androidx.compose.runtime.MutableIntState,
    showEnvVarCreateDialogRef: androidx.compose.runtime.MutableState<Boolean>,
    showAddDriveDialogRef: androidx.compose.runtime.MutableState<Boolean>,
    selectedDriveLetterRef: androidx.compose.runtime.MutableState<String>,
    pendingDriveLetterRef: androidx.compose.runtime.MutableState<String>,
    driveLetterMenuExpandedRef: androidx.compose.runtime.MutableState<Boolean>,
    nonDeletableDriveLetters: Set<String>,
    availableDriveLetters: List<String>,
    launchManifestInstall: (ManifestEntry, String, Boolean, com.winlator.contents.ContentProfile.ContentType?, () -> Unit) -> Unit,
    launchManifestContentInstall: (ManifestEntry, com.winlator.contents.ContentProfile.ContentType, () -> Unit) -> Unit,
    launchManifestDriverInstall: (ManifestEntry, () -> Unit) -> Unit,
    getStartupSelectionOptions: () -> List<String>,
    launchFolderPicker: () -> Unit,
    getVersionsForDriver: () -> List<String>,
    getVersionsForBox64: () -> ManifestComponentHelper.VersionOptionList,
    applyScreenSizeToConfig: () -> Unit,
    vkd3dForcedVersion: () -> String,
    currentDxvkContext: () -> ManifestComponentHelper.DxvkContext,
): ContainerConfigState = ContainerConfigState(
    config = configState,
    graphicsDrivers = graphicsDriversRef,
    bionicWineEntries = bionicWineEntriesRef,
    glibcWineEntries = glibcWineEntriesRef,
    wrapperVersions = wrapperVersionsRef,
    dxvkVersionsAll = dxvkVersionsAllRef,
    componentAvailability = componentAvailabilityRef,
    showCustomResolutionDialog = showCustomResolutionDialogRef,
    customResolutionValidationError = customResolutionValidationErrorRef,
    vkMaxVersionIndex = vkMaxVersionIndexRef,
    imageCacheIndex = imageCacheIndexRef,
    exposedExtIndices = exposedExtIndicesRef,
    maxDeviceMemoryIndex = maxDeviceMemoryIndexRef,
    bionicDriverIndex = bionicDriverIndexRef,
    wrapperVersionIndex = wrapperVersionIndexRef,
    presentModeIndex = presentModeIndexRef,
    resourceTypeIndex = resourceTypeIndexRef,
    bcnEmulationIndex = bcnEmulationIndexRef,
    bcnEmulationTypeIndex = bcnEmulationTypeIndexRef,
    bcnEmulationCacheEnabled = bcnEmulationCacheEnabledRef,
    disablePresentWaitChecked = disablePresentWaitCheckedRef,
    syncEveryFrameChecked = syncEveryFrameCheckedRef,
    sharpnessEffectIndex = sharpnessEffectIndexRef,
    sharpnessLevel = sharpnessLevelRef,
    sharpnessDenoise = sharpnessDenoiseRef,
    adrenotoolsTurnipChecked = adrenotoolsTurnipCheckedRef,
    emulator64Index = emulator64IndexRef,
    emulator32Index = emulator32IndexRef,
    screenSizeIndex = screenSizeIndexRef,
    customScreenWidth = customScreenWidthRef,
    customScreenHeight = customScreenHeightRef,
    graphicsDriverIndex = graphicsDriverIndexRef,
    dxWrapperIndex = dxWrapperIndexRef,
    dxvkVersionIndex = dxvkVersionIndexRef,
    graphicsDriverVersionIndex = graphicsDriverVersionIndexRef,
    audioDriverIndex = audioDriverIndexRef,
    gpuNameIndex = gpuNameIndexRef,
    renderingModeIndex = renderingModeIndexRef,
    videoMemIndex = videoMemIndexRef,
    mouseWarpIndex = mouseWarpIndexRef,
    externalDisplayModeIndex = externalDisplayModeIndexRef,
    languageIndex = languageIndexRef,
    showEnvVarCreateDialog = showEnvVarCreateDialogRef,
    showAddDriveDialog = showAddDriveDialogRef,
    selectedDriveLetter = selectedDriveLetterRef,
    pendingDriveLetter = pendingDriveLetterRef,
    driveLetterMenuExpanded = driveLetterMenuExpandedRef,
    screenSizes = resources.screenSizes,
    baseGraphicsDrivers = resources.baseGraphicsDrivers,
    dxWrappers = resources.dxWrappers,
    dxvkVersionsBase = resources.dxvkVersionsBase,
    vkd3dVersionsBase = resources.vkd3dVersionsBase,
    audioDrivers = resources.audioDrivers,
    presentModes = resources.presentModes,
    resourceTypes = resources.resourceTypes,
    bcnEmulationEntries = resources.bcnEmulationEntries,
    bcnEmulationTypeEntries = resources.bcnEmulationTypeEntries,
    sharpnessEffects = resources.sharpnessEffects,
    sharpnessDisplayItems = resources.sharpnessDisplayItems,
    renderingModes = resources.renderingModes,
    videoMemSizes = resources.videoMemSizes,
    mouseWarps = resources.mouseWarps,
    externalDisplayModes = resources.externalDisplayModes,
    winCompOpts = resources.winCompOpts,
    box64Versions = resources.box64Versions,
    wowBox64VersionsBase = resources.wowBox64VersionsBase,
    box64BionicVersionsBase = resources.box64BionicVersionsBase,
    fexcoreVersionsBase = resources.fexcoreVersionsBase,
    fexcoreTSOPresets = resources.fexcoreTSOPresets,
    fexcoreX87Presets = resources.fexcoreX87Presets,
    fexcoreMultiblockValues = resources.fexcoreMultiblockValues,
    startupSelectionEntries = resources.startupSelectionEntries,
    turnipVersions = resources.turnipVersions,
    virglVersions = resources.virglVersions,
    zinkVersions = resources.zinkVersions,
    vortekVersions = resources.vortekVersions,
    adrenoVersions = resources.adrenoVersions,
    sd8EliteVersions = resources.sd8EliteVersions,
    containerVariants = resources.containerVariants,
    bionicWineEntriesBase = resources.bionicWineEntriesBase,
    glibcWineEntriesBase = resources.glibcWineEntriesBase,
    emulatorEntries = resources.emulatorEntries,
    bionicGraphicsDrivers = resources.bionicGraphicsDrivers,
    baseWrapperVersions = resources.baseWrapperVersions,
    languages = resources.languages,
    dxvkOptions = resources.dxvkOptions,
    vkd3dOptions = resources.vkd3dOptions,
    box64Options = resources.box64Options,
    box64BionicOptions = resources.box64BionicOptions,
    wowBox64Options = resources.wowBox64Options,
    fexcoreOptions = resources.fexcoreOptions,
    wrapperOptions = resources.wrapperOptions,
    bionicWineOptions = resources.bionicWineOptions,
    glibcWineOptions = resources.glibcWineOptions,
    dxvkManifestById = resources.dxvkManifestById,
    vkd3dManifestById = resources.vkd3dManifestById,
    box64ManifestById = resources.box64ManifestById,
    wowBox64ManifestById = resources.wowBox64ManifestById,
    fexcoreManifestById = resources.fexcoreManifestById,
    wrapperManifestById = resources.wrapperManifestById,
    bionicWineManifestById = resources.bionicWineManifestById,
    glibcWineManifestById = resources.glibcWineManifestById,
    gpuCards = resources.gpuCards,
    box64Presets = resources.box64Presets,
    fexcorePresets = resources.fexcorePresets,
    gpuExtensions = resources.gpuExtensions,
    inspectionMode = resources.inspectionMode,
    isBionicVariant = resources.isBionicVariant,
    nonDeletableDriveLetters = nonDeletableDriveLetters,
    availableDriveLetters = availableDriveLetters,
    launchManifestInstall = launchManifestInstall,
    launchManifestContentInstall = launchManifestContentInstall,
    launchManifestDriverInstall = launchManifestDriverInstall,
    getStartupSelectionOptions = getStartupSelectionOptions,
    launchFolderPicker = launchFolderPicker,
    getVersionsForDriver = getVersionsForDriver,
    getVersionsForBox64 = getVersionsForBox64,
    applyScreenSizeToConfig = applyScreenSizeToConfig,
    vkd3dForcedVersion = vkd3dForcedVersion,
    currentDxvkContext = currentDxvkContext,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ContainerConfigDialogChrome(
    title: String,
    default: Boolean,
    initialConfig: ContainerData,
    state: ContainerConfigState,
    dismissDialogState: MessageDialogState,
    showManifestDownloadDialog: Boolean,
    manifestDownloadProgress: Float,
    manifestDownloadMessage: String,
    nonzeroResolutionError: String,
    aspectResolutionError: String,
    onDismissDialog: () -> Unit,
    onDismissCheck: () -> Unit,
    onConfirmDismiss: () -> Unit,
    onSave: (ContainerData) -> Unit,
) {
    val config = state.config.value

    LoadingDialog(
        visible = showManifestDownloadDialog,
        progress = manifestDownloadProgress,
        message = manifestDownloadMessage,
    )

    MessageDialog(
        visible = dismissDialogState.visible,
        title = dismissDialogState.title,
        message = dismissDialogState.message,
        confirmBtnText = dismissDialogState.confirmBtnText,
        dismissBtnText = dismissDialogState.dismissBtnText,
        onDismissRequest = onDismissDialog,
        onDismissClick = onDismissDialog,
        onConfirmClick = onConfirmDismiss,
    )

    Dialog(
        onDismissRequest = onDismissCheck,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
        ),
    ) {
        val scrollState = rememberScrollState()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "$title${if (initialConfig != config) "*" else ""}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismissCheck) {
                            Icon(Icons.Default.Close, null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { onSave(config) }) {
                            Icon(Icons.Default.Save, null)
                        }
                    },
                )
            },
        ) { paddingValues ->
            var selectedTab by rememberSaveable { mutableIntStateOf(0) }
            val tabs = listOf(
                stringResource(R.string.container_config_tab_general),
                stringResource(R.string.container_config_tab_graphics),
                stringResource(R.string.container_config_tab_emulation),
                stringResource(R.string.container_config_tab_controller),
                stringResource(R.string.container_config_tab_wine),
                stringResource(R.string.container_config_tab_win_components),
                stringResource(R.string.container_config_tab_environment),
                stringResource(R.string.container_config_tab_drives),
                stringResource(R.string.container_config_tab_advanced),
            )

            Column(
                modifier = Modifier
                    .padding(
                        top = app.gamenative.utils.PaddingUtils.statusBarAwarePadding().calculateTopPadding() +
                            paddingValues.calculateTopPadding(),
                        bottom = 32.dp + paddingValues.calculateBottomPadding(),
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                    )
                    .fillMaxSize(),
            ) {
                androidx.compose.material3.ScrollableTabRow(selectedTabIndex = selectedTab, edgePadding = 0.dp) {
                    tabs.forEachIndexed { index, label ->
                        androidx.compose.material3.Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(text = label) },
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .weight(1f),
                ) {
                    if (selectedTab == 0) GeneralTabContent(state, nonzeroResolutionError, aspectResolutionError)
                    if (selectedTab == 1) GraphicsTabContent(state)
                    if (selectedTab == 2) EmulationTabContent(state)
                    if (selectedTab == 3) ControllerTabContent(state, default)
                    if (selectedTab == 4) WineTabContent(state)
                    if (selectedTab == 5) WinComponentsTabContent(state)
                    if (selectedTab == 6) EnvironmentTabContent(state)
                    if (selectedTab == 7) DrivesTabContent(state)
                    if (selectedTab == 8) AdvancedTabContent(state)
                }
            }
        }
    }
}
