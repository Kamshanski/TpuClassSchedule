package edu.kamshanski.tpuclassschedule.activities._abstract


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Основа фрагмента с соответствующим объектом [ViewBinding]. Для его создания используется рефлексия.
 * Раньше тут был [ViewModel], но теперь он заменён на Котлиновские расширения.
 *
 * Поочереди позволяет
 *  - сконфигурировать View'шки,
 *  - подписаться на [ViewModel],
 *  - установить Listener'ы на  View'шки.
 */
abstract class BaseFragment : Fragment() {

    // Methods to reimplement
    /** сконфигурировать View'шки: начальные значения, размеры, цвета, картинки, создание объектов программно */
    protected open fun configureViews() {}
    /** подписаться на [ViewModel]: усановить [LiveData.observe], записать начальные значения туда же*/
    protected open fun subscribeToViewModel() {}
    /** установить Listener'ы на  View'шки*/
    protected open fun setViewListeners() {}

    open fun goToInitState() {}

    // Base

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)  {
        super.onViewCreated(view, savedInstanceState)
        //initViewModel<ViewModel>()    // для создания ViewModel с помощью рефлексии
        configureViews()
        subscribeToViewModel()
        setViewListeners()
    }



    private lateinit var inflate: Method
    private lateinit var bindingField: Field
//    private lateinit var viewModelField: Field

    init {
        var foundBinding = false
//        var foundViewModel = false
        for (declaredField in this.javaClass.declaredFields) {
            if (ViewBinding::class.java.isAssignableFrom(declaredField.type)) {
                bindingField = declaredField
                bindingField.isAccessible = true
                for (method in bindingField.type.methods) {
                    if (method.parameterTypes.size == 3) {
                        inflate = method
                        foundBinding = true
                        break
                    }
                }
//            } else if (ViewModel::class.java.isAssignableFrom(declaredField.type)) {
//                viewModelField = declaredField
//                viewModelField.isAccessible = true
//                foundViewModel = true
            }

            if (foundBinding
//                && foundViewModel
            ) {
                break
            }
        }
    }



// Позволяет создавать ViewModel с помощью рефлексии
//    @SuppressWarnings("unchecked")
//    private fun <T : ViewModel> initViewModel() {
//        try {
//            val vm = ViewModelProvider(this).get(viewModelField.type as Class<T>);
//            viewModelField.set(this, vm);
//        } catch (e: IllegalAccessException) {
//            e.printStackTrace()
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) : View? {
        try {
            var binding = bindingField[this] as ViewBinding?
            if (binding == null) {
                binding = inflate.invoke(null, inflater, container, false) as ViewBinding
                bindingField[this] = binding
            }
            return binding.root
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        try {
            bindingField[this] = null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        super.onDestroyView()
//        requireActivity().onBackPressedDispatcher.addCallback(this) {
//            val navController = findNavController()
//            if (navController.currentBackStackEntry?.destination?.id != null) {
//                findNavController().popBackStackAllInstances(
//                    navController.currentBackStackEntry?.destination?.id!!,
//                    true
//                )
//            } else
//                navController.popBackStack()
//        }
    }
}

