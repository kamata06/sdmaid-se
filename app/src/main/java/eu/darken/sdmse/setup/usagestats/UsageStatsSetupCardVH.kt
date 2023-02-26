package eu.darken.sdmse.setup.usagestats

import android.view.ViewGroup
import androidx.core.view.isGone
import eu.darken.sdmse.R
import eu.darken.sdmse.common.lists.binding
import eu.darken.sdmse.databinding.SetupUsagestatsItemBinding
import eu.darken.sdmse.setup.SetupAdapter


class UsageStatsSetupCardVH(parent: ViewGroup) :
    SetupAdapter.BaseVH<UsageStatsSetupCardVH.Item, SetupUsagestatsItemBinding>(
        R.layout.setup_usagestats_item,
        parent
    ) {


    override val viewBinding = lazy { SetupUsagestatsItemBinding.bind(itemView) }

    override val onBindData: SetupUsagestatsItemBinding.(
        item: Item,
        payloads: List<Any>
    ) -> Unit = binding { item ->
        grantState.isGone = item.setupState.missingPermission.isNotEmpty()

        grantAction.setOnClickListener { item.onGrantAction() }
        helpAction.setOnClickListener { item.onHelp() }
    }

    data class Item(
        val setupState: UsageStatsSetupModule.State,
        val onGrantAction: () -> Unit,
        val onHelp: () -> Unit,
    ) : SetupAdapter.Item {
        override val stableId: Long = this.javaClass.hashCode().toLong()
    }

}