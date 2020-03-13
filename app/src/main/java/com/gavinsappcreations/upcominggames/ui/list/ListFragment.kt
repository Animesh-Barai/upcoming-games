package com.gavinsappcreations.upcominggames.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.gavinsappcreations.upcominggames.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private val viewModel: ListViewModel by lazy {
        ViewModelProvider(this, ListViewModel.Factory(requireActivity().application)).get(
            ListViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the GameListViewModel
        binding.viewModel = viewModel

        binding.gameRecyclerView.itemAnimator = null

        val adapter = GameGridAdapter(GameGridAdapter.OnClickListener {
            viewModel.onNavigateToDetailFragment(it)
        })

        binding.gameRecyclerView.adapter = adapter

        binding.sortImageButton.setOnClickListener {
            viewModel.onNavigateToSortFragment()
        }

        binding.searchTextView.setOnClickListener {
            viewModel.onNavigateToSearchFragment()

            //viewModel.getAllGames()
        }

        viewModel.gameList.observe(viewLifecycleOwner, Observer {
            viewModel.updateDatabaseState()
        })

        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { game ->
                findNavController(this).navigate(
                    ListFragmentDirections.actionListFragmentToDetailFragment(
                        game.guid
                    )
                )
            }
        })

        viewModel.navigateToSortFragment.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                findNavController(this).navigate(ListFragmentDirections.actionListFragmentToSortFragment())
            }
        })

        viewModel.navigateToSearchFragment.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                findNavController(this).navigate(ListFragmentDirections.actionListFragmentToSearchFragment())
            }
        })

        return binding.root
    }


}


