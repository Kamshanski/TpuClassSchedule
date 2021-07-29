package edu.kamshanski.tpuclassschedule.activities._abstract

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

abstract class BaseAppCompatActivity : AppCompatActivity() {
    // Methods to reimplement
    /** сконфигурировать View'шки: начальные значения, размеры, цвета, картинки, создание объектов программно */
    protected open fun initViews() {}
    /** подписаться на [ViewModel]: усановить [LiveData.observe], записать начальные значения туда же*/
    protected open fun initViewModel() {}
    /** установить Listener'ы на  View'шки*/
    protected open fun initListeners() {}

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
                    if (method.parameterTypes.size == 1 && method.parameterTypes[0] == LayoutInflater::class.java) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            var binding = bindingField[this] as ViewBinding?
            if (binding == null) {
                binding = inflate.invoke(null, layoutInflater) as ViewBinding
                bindingField[this] = binding
            }
            setContentView(binding.root)

            //initViewModel<ViewModel>()    // для создания ViewModel с помощью рефлексии
            initViews()
            initViewModel()
            initListeners()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        return super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        try {
            bindingField[this] = null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        super.onDestroy()
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

    fun runWhenStarted(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }
}