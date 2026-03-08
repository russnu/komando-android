package org.russel.komandoandroid.data.model

import org.russel.komandoandroid.R

sealed class NavItem (
    val route: String,
    val label: String,
    val icon: Int
){
    object Tasks : NavItem("tasks", "Tasks", R.drawable.ic_tasks)

    object Groups : NavItem("groups", "Groups", R.drawable.ic_group)
    object Profile : NavItem("profile", "Profile", R.drawable.ic_profile)
}