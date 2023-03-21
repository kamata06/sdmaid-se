package eu.darken.sdmse.scheduler.ui.manager

import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import eu.darken.sdmse.R
import eu.darken.sdmse.common.lists.binding
import eu.darken.sdmse.common.toSystemTimezone
import eu.darken.sdmse.databinding.SchedulerManagerListItemBinding
import eu.darken.sdmse.scheduler.core.Schedule
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class SchedulerRowVH(parent: ViewGroup) :
    SchedulerAdapter.BaseVH<SchedulerRowVH.Item, SchedulerManagerListItemBinding>(
        R.layout.scheduler_manager_list_item,
        parent
    ) {

    override val viewBinding = lazy { SchedulerManagerListItemBinding.bind(itemView) }

    override val onBindData: SchedulerManagerListItemBinding.(
        item: Item,
        payloads: List<Any>
    ) -> Unit = binding { item ->
        val schedule = item.schedule

        title.text = schedule.label

        subtitle.apply {
            val days = schedule.repeatInterval.toDays()
            val daysText = getQuantityString(R.plurals.scheduler_schedule_repeat_x_days, days.toInt())

            val localTime = LocalTime.of(schedule.hour, schedule.minute)

            val hourTxt = schedule.hour.toString().padStart(2, '0')
            val minuteTxt = schedule.minute.toString().padStart(2, '0')
            val time = "$hourTxt:$minuteTxt"

            text = getString(R.string.scheduler_current_schedule_x_at_x, daysText, time)
        }

        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

        primary.apply {
            isVisible = schedule.scheduledAt != null
            text = schedule.nextExecution?.let { startedAt ->
                val next = startedAt.toSystemTimezone()
                val formatted = next.format(formatter)
                getString(R.string.scheduler_schedule_next_at_x, formatted)
            }
        }

        secondary.apply {
            isVisible = schedule.executedAt != null
            text = schedule.executedAt?.let {
                val local = it.toSystemTimezone().format(formatter)
                getString(R.string.scheduler_schedule_last_at_x, local)
            }
        }

        enabledToggle.apply {
            setOnClickListener(null)
            isChecked = schedule.isEnabled
            setOnCheckedChangeListener { _, _ -> item.onToggle() }
            text = when (schedule.isEnabled) {
                true -> getString(R.string.scheduler_schedule_toggle_enabled)
                else -> getString(R.string.scheduler_schedule_toggle_disabled)
            }
        }

        optionsContainer.isGone = schedule.isEnabled

        editAction.apply {
            isGone = schedule.isEnabled
            setOnClickListener { item.onEdit() }
        }

        removeAction.apply {
            isGone = schedule.isEnabled
            setOnClickListener { item.onRemove() }
        }

        toolCorpsefinderToggle.apply {
            setOnCheckedChangeListener(null)
            isChecked = schedule.useCorpseFinder
            setOnCheckedChangeListener { _, _ -> item.onToggleCorpseFinder() }
        }
        toolSystemcleanerToggle.apply {
            setOnCheckedChangeListener(null)
            isChecked = schedule.useSystemCleaner
            setOnCheckedChangeListener { _, _ -> item.onToggleSystemCleaner() }
        }
        toolAppcleanerToggle.apply {
            setOnCheckedChangeListener(null)
            isChecked = schedule.useAppCleaner
            setOnCheckedChangeListener { _, _ -> item.onToggleAppCleaner() }
        }
    }


    data class Item(
        val schedule: Schedule,
        val onEdit: () -> Unit,
        val onToggle: () -> Unit,
        val onRemove: () -> Unit,
        val onToggleCorpseFinder: () -> Unit,
        val onToggleSystemCleaner: () -> Unit,
        val onToggleAppCleaner: () -> Unit,
    ) : SchedulerAdapter.Item {

        override val stableId: Long = schedule.id.hashCode().toLong()
    }
}